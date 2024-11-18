package com.secondserve.security;

import com.secondserve.jwt.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider; // JWT 토큰 생성 및 검증 클래스
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request); // 요청 헤더에서 토큰 추출
        if (token != null) {
            System.out.println(token);
            if (jwtProvider.verifyToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token); // 토큰에서 사용자 정보 추출
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 저장
                System.out.println("Authentication 성공: " + authentication.getName());
            } else {
                System.out.println("토큰 유효성 검사 실패");
            }
        } else {
            System.out.println("Authorization 헤더에 유효한 토큰이 없음");
        }
        filterChain.doFilter(request, response); // 다음 필터로 진행
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // "Bearer " 부분을 제거하고 줄바꿈 및 공백을 모두 제거
            return bearerToken.substring(7).replaceAll("\\s+", "");
        }
        return null;
    }


}
