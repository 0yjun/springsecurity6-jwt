package com.security.config;

import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
                 .csrf(csrf -> csrf.disable())
                 .authorizeHttpRequests(auth->auth
                         .anyRequest().authenticated()
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                 )
                 .logout(logout->logout.permitAll());
         return http.build();
    }

}
