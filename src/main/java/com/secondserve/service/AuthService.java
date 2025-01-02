//package com.secondserve.service;
//
//import com.secondserve.dto.LoginRequest;
//import com.secondserve.dto.TokenResponseDto;
//import com.secondserve.jwt.CookieUtil;
//import com.secondserve.jwt.JwtConstants;
//import com.secondserve.jwt.JwtProvider;
//import com.secondserve.repository.CustomerRepository;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import org.springframework.http.HttpHeaders;
//
//@Service
//@AllArgsConstructor
//public class AuthService {
//
//    @Autowired
//    private final CustomerRepository customerRepository;
//    @Autowired
//    private final CookieUtil cookieUtil;
//    //private final JwtProvider jwtProvider;
//
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//
//    public Authentication authenticate(LoginRequest loginRequest) {
//        // 사용자의 이메일과 비밀번호로 인증 토큰 생성
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());
//        // AuthenticationManager를 통해 인증 처리
//        return authenticationManager.authenticate(authenticationToken);
//    }
//    public TokenResponseDto generateToken(UserDetails userDetails){
//        String accessToken = jwtProvider.generateAccessToken(userDetails);
//        String refreshToken = jwtProvider.generateRefreshToken(userDetails);
//
//        Cookie refreshCookie = cookieUtil.addCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME.toString(), refreshToken, 30);
//
//        return new TokenResponseDto(accessToken, refreshCookie);
//    }
//
//    public HttpHeaders setResponseHeaderWithToken(String accessToken){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        return headers;
//    }
//}
