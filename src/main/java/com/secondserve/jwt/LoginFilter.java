package com.secondserve.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondserve.dto.LoginRequest;
import com.secondserve.entity.RefreshEntity;
import com.secondserve.repository.RefreshRepository;
import com.secondserve.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final int COOKIE_EXPIRE_TIME = 30 * 60; // 30분
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7일

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("Attempting authentication...");
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            LoginRequest loginRequest = new ObjectMapper().readValue(messageBody, LoginRequest.class);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword(), null);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        log.info("Authentication successful...");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String id = userDetails.getCustomerId();
        String email = userDetails.getUsername();
        try {
            String access = jwtUtil.createJwt("access", id, email, ACCESS_TOKEN_EXPIRE_TIME);
            response.addHeader("access", access);
        } catch (Exception e) {
            log.error("Error generating JWT: ", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        String refresh = jwtUtil.createJwt("refresh", id, email, REFRESH_TOKEN_EXPIRE_TIME);

        addRefreshEntity(email, refresh, REFRESH_TOKEN_EXPIRE_TIME);

        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        response.getHeaderNames().forEach(headerName ->
                log.info("Response Header: {} = {}", headerName, response.getHeader(headerName))
        );
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        RefreshEntity refreshEntity = new RefreshEntity(email, refresh, new Date(System.currentTimeMillis() + expiredMs).toString());
        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_EXPIRE_TIME);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
