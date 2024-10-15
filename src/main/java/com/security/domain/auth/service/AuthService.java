package com.security.domain.auth.service;


import com.security.domain.auth.model.reqest.LoginRequest;
import com.security.domain.auth.model.reqest.SignupRequest;
import com.security.domain.auth.model.response.LoginResponse;
import com.security.domain.auth.model.response.SignupResponse;
import com.security.domain.user.entry.User;
import com.security.domain.user.entry.UserCredential;
import com.security.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
        //UserDetails userDetails = this.loadUserByUsername(request.getName());
        try{
            User newUser = this.newUser(request.getName());
            UserCredential userCredential = this.newUsercrUserCredential(request.getPassword(),newUser);
            newUser.setUserCredential(userCredential);
            User savedUser = userRepository.save(newUser);
            log.error("user save");
        }catch(Exception e){
            //TODO 반환
            log.error("user save error");
            e.printStackTrace();
            return null;
        }
        return new SignupResponse(request.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름을 통해 사용자 정보를 로드
        Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        log.error("Stored password: " + user.get().getUserCredential().getPassword()); // 데이터베이스에 저장된 비밀번호
        return new UserCredential(user.get());
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
        try {
            log.error("requset name "+request.getName());
            log.error("requset password "+request.getPassword());
            log.error("encoded password "+passwordEncoder.encode(request.getPassword()));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(),request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);


            //Authentication authentication = new UsernamePasswordAuthenticationToken(request.getName(),request.getPassword());
            //SecurityContextHolder.getContext().setAuthentication(authentication);
            // 사용자 인증 정보를 가져온 후 처리
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.isAuthenticated()) {
                // TODO: 토큰 생성 등의 추가 로직
                log.error("토큰 발급 성공");
                return new LoginResponse(request.getName());
            } else {
                log.error("토큰 발급 실패");
                // TODO: 인증 실패 처리
                return null;
            }
        }catch (Exception e){
            //TODO Exception 구현
            log.error("로그인 실패 ",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
