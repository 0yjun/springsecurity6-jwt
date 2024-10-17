package com.security.domain.auth.service;


import com.security.common.exception.CustomException;
import com.security.common.exception.ErrorCode;
import com.security.domain.auth.model.reqest.LoginRequest;
import com.security.domain.auth.model.reqest.SignupRequest;
import com.security.domain.auth.model.response.LoginResponse;
import com.security.domain.auth.model.response.SignupResponse;
import com.security.domain.user.entry.User;
import com.security.domain.user.entry.UserCredential;
import com.security.domain.user.repository.UserRepository;
import com.security.security.JWTProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository
            ,@Lazy AuthenticationManager authenticationManager
            , PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /* **************************************************************************************************************
    * * 회원가입 구현
    * **************************************************************************************************************/

    public SignupResponse register(SignupRequest request){
        log.error("user register");
        Optional<User> findUser = userRepository.findByName(request.getName());
        if(findUser.isPresent()){
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }
        try{
            User newUser = this.newUser(request.getName());
            UserCredential userCredential = this.newUsercrUserCredential(request.getPassword(),newUser);
            newUser.setUserCredential(userCredential);
            User savedUser = userRepository.save(newUser);
            log.error("user save");
        }catch(Exception e){
            throw new CustomException(ErrorCode.USER_SAVE_FAIL);
        }
        return new SignupResponse(request.getName());
    }

    public User newUser(String name){
        return User.builder()
                .name(name)
                .build();
    }

    public UserCredential newUsercrUserCredential(String password, User user){
        String hashValue = passwordEncoder.encode(password);
        return UserCredential.
                builder().
                user(user).
                password(hashValue).
                build();

    }

    /* **************************************************************************************************************
     * * 회원가입 구현 END
     * **************************************************************************************************************/

    /* **************************************************************************************************************
     * * 로그인 START
     * **************************************************************************************************************/

    public LoginResponse login(LoginRequest request){
        /* 아이디 비밀번호 체크 */
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(),request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.MIS_MATCH_PASSWORD, e.getMessage());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /* 토큰 생성 */
        if (auth.isAuthenticated()) {
            // TODO: 토큰 생성 등의 추가 로직
            log.error("토큰 발급 성공");
            try{
                String token = JWTProvider.createToken(request.getName());
                return new LoginResponse(ErrorCode.SUCCESS, token);
            }catch(IllegalArgumentException e){
                e.printStackTrace();
                throw new CustomException(ErrorCode.ACCESS_TOKEN_IS_NOT_CREATED);
            }
        } else {
            throw new CustomException(ErrorCode.TOKEN_IS_INVALID);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름을 통해 사용자 정보를 로드
        Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return UserCredential.builder()
                .user(user.get())
                .password(user.get().getUserCredential().getPassword())
                .build();
    }

    /* **************************************************************************************************************
     * * 로그인 END
     * **************************************************************************************************************/

    public String getUserFromToken(String token){
        return JWTProvider.getUserFromToken(token);
    }

}
