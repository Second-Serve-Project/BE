package com.upcraft.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;


    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
    }

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token); // JWT에서 Claims 추출
        String userId = claims.get("id", String.class); // Claims에서 사용자 ID 추출
        String role = claims.get("role", String.class); // Claims에서 역할 추출

        // 사용자의 권한을 담은 리스트 생성
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // UserDetails 객체 생성 (사용자 정보 포함)
        UserDetails userDetails = new User(userId, "", authorities);

        // Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 100 * 10);

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(jwtProperties.getIssuer())
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("id", userDetails.getUsername())
                .compact();
    }
    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 10); // 1시간 후 만료

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(jwtProperties.getIssuer())
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("id", userDetails.getUsername())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 60 * 1000); // 1주일 후 만료

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(jwtProperties.getIssuer())
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("id", userDetails.getUsername())
                .compact();
    }

    // 토큰 검증 로직
    public boolean verifyToken(String token) {
        try {
            Claims claims = parseClaims(token);

            if (claims == null){return false;}
            if (claims.getExpiration().before(new Date())){
                logger.warn("JWT Token is expired.");
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }


    // Claims 파싱 (중복 코드 제거)
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            logger.warn("Expired JWT token: {}", e.getMessage());
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    // ID 추출
    public String getId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id", String.class);
    }

    public String extractTokenFromCookie(HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        for(Cookie cookie: cookies){
            if (cookie.getName().equals("refresh_token")) { // 쿠키 이름이 "refresh_token"인지 확인
                return cookie.getValue(); // 쿠키 값 (즉, 토큰)을 반환
            }
        }
        return null;
    }

}
