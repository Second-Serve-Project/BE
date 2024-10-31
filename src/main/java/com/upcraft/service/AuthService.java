package com.upcraft.service;

import com.upcraft.dto.LoginRequest;
import com.upcraft.dto.TokenResponseDto;
import com.upcraft.jwt.CookieUtil;
import com.upcraft.jwt.JwtConstants;
import com.upcraft.jwt.JwtProvider;
import com.upcraft.repository.CustomerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;

@Service
@AllArgsConstructor
public class AuthService {

    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Authentication authenticate(LoginRequest loginRequest) {
        // 사용자의 이메일과 비밀번호로 인증 토큰 생성

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());
        // AuthenticationManager를 통해 인증 처리
        return authenticationManager.authenticate(authenticationToken);
    }
    public TokenResponseDto generateToken(UserDetails userDetails){
        String accessToken = jwtProvider.generateAccessToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);

        Cookie refreshCookie = cookieUtil.addCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME.toString(), refreshToken, 30);

        return new TokenResponseDto(accessToken, refreshCookie);
    }
    public TokenResponseDto reissueToken(HttpServletRequest request) {
        String refreshToken = jwtProvider.extractTokenFromCookie(request);
        if (jwtProvider.verifyToken(refreshToken)) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtProvider.getId(refreshToken));
            String accessToken = jwtProvider.generateAccessToken(userDetails);
            return new TokenResponseDto(accessToken, null); // 재발급 시 쿠키는 필요 없을 수도 있음
        }
        return null; // 재발급 실패 시 null 반환 (또는 예외 처리)
    }
    public HttpHeaders setResponseHeaderWithToken(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
