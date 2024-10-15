//package com.security.domain.user.service;
//
//
//import com.security.domain.user.model.response.UserResponse;
//import com.security.domain.user.repository.UserRepository;
//import com.security.domain.user.entry.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserService {
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//
//    public UserResponse findByName(String name){
//        Optional<User> user = userRepository.findUserByNameLike(name);
//        if(user.isEmpty()){
//            return null;
//        }else{
//            return modelMapper.map(user.get(), UserResponse.class);
//        }
//    }
//}
