package com.security.security;

import com.security.common.exception.CustomException;
import com.security.common.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;

    public JWTFilter(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain
    ) throws ServletException, IOException {
        String authorization= request.getHeader("Authorization");
        log.error("Authorization "+authorization);
        String token = authorization.split(" ")[1];

        if(!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")){
            log.error("token null");
            filterChain.doFilter(request,response);
            throw new CustomException(ErrorCode.ACCESS_TOKEN_IS_NOT_EXIST);
        }else if(1==2){
            //TODO token expire 코드 작성
        }

        String username = jwtProvider.getUserFromToken(token);
        log.error("user name is "+username);

    }
}
