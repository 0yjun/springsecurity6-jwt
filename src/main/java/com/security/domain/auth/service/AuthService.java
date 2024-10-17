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
import com.security.security.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthService(
            UserRepository userRepository
            ,@Lazy AuthenticationManager authenticationManager
            , PasswordEncoder passwordEncoder
            , JWTUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(),request.getPassword())
            );
            final UserDetails userDetails = loadUserByUsername(request.getName());
            final String jwt = jwtUtil.createToken(request.getName());
            return new LoginResponse(ErrorCode.SUCCESS_LOGIN, jwt);
        }catch(AuthenticationException e){
            throw new CustomException(ErrorCode.MIS_MATCH_PASSWORD,e.getMessage());
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
        return JWTUtil.getUserFromToken(token);
    }

}
