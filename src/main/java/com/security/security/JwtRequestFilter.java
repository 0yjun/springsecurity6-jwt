package com.security.security;

import com.security.common.exception.CustomException;
import com.security.common.exception.ErrorCode;
import com.security.domain.auth.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 로그인 및 회원가입 요청인 경우 필터를 통과
        if ("/api/auth/login".equals(path) || "/api/auth/signup".equals(path)) {
            chain.doFilter(request, response);
            return; // JWT 검증 로직 생략
        }


        final String authorizationHeader = request.getHeader("Authorization");
        String username =null;
        String jwt = null;

        if(!StringUtils.hasText(username) && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            try {
                // JWT에서 사용자명 추출 (만료되었을 경우 예외 발생)
                username = jwtUtil.getUserFromToken(jwt);
            } catch (ExpiredJwtException e) {
                // 만료된 토큰의 경우 예외 처리
                throw new CustomException(ErrorCode.ACCESS_TOKEN_IS_EXPIRED,e.getMessage());
            } catch (Exception e) {
                throw new CustomException(ErrorCode.TOKEN_IS_INVALID,e.getMessage());
            }
        }

        /**/
        if(StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.authService.loadUserByUsername(username);

            try{
                if(jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }catch(ExpiredJwtException e){
                throw new CustomException(ErrorCode.ACCESS_TOKEN_IS_EXPIRED,e.getMessage());
            }catch (Exception e) {
                throw new CustomException(ErrorCode.TOKEN_IS_INVALID,e.getMessage());
            }

        }
        chain.doFilter(request,response);
    }
}
