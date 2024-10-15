package com.security.domain.auth.service;


import com.security.domain.auth.model.reqest.SignupRequest;
import com.security.domain.auth.model.response.SignupResponse;
import com.security.domain.user.entry.User;
import com.security.domain.user.entry.UserCredential;
import com.security.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public SignupResponse register(SignupRequest request){
        Optional<User> findUser = userRepository.findByName(request.getName());

        if(findUser.isPresent()){
            //TODO Error로 변환
            return null;
        }
        try{
            User newUser = this.newUser(request.getName());
            UserCredential userCredential = this.newUsercrUserCredential(request.getPassword(),newUser);
            newUser.setUserCredential(userCredential);
            User savedUser = userRepository.save(newUser);
            if(savedUser==null){
                //TODO ERROr변환
                return null;
            }
        }catch(Exception e){
            //TODO 반환
            return null;
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

        UserCredential cre = UserCredential.
                builder().
                user(user).
                password(hashValue).
                build();

        return cre;

    }
}
