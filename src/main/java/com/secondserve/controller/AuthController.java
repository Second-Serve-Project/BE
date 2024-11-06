package com.secondserve.controller;

import com.secondserve.dto.LoginRequest;
import com.secondserve.dto.TokenResponseDto;
import com.secondserve.jwt.CookieUtil;
import com.secondserve.jwt.JwtProvider;
import com.secondserve.service.AuthService;
import com.secondserve.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authService.authenticate(loginRequest);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        TokenResponseDto tokenResponse = authService.generateToken(userDetails);

        HttpHeaders headers = authService.setResponseHeaderWithToken(tokenResponse.getAccessToken());
        response.addCookie(tokenResponse.getRefreshCookie());

        return ResponseEntity.ok()
                .headers(headers)
                .body(tokenResponse.getAccessToken());
    }

    @GetMapping("/gentoken")
    public ResponseEntity<String> genToken(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        TokenResponseDto tokenResponse = authService.generateToken(userDetails);

        HttpHeaders headers = authService.setResponseHeaderWithToken(tokenResponse.getAccessToken());
        response.addCookie(tokenResponse.getRefreshCookie());

        return ResponseEntity.ok()
                .headers(headers)
                .body(tokenResponse.getAccessToken());
    }
    @GetMapping("/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response){
        TokenResponseDto tokenResponse = authService.reissueToken(request);
        if(tokenResponse != null){
            return ResponseEntity.ok()
                    .headers(authService.setResponseHeaderWithToken(tokenResponse.getAccessToken()))
                    .body(tokenResponse.getAccessToken());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID TOKEN");
    }
}
