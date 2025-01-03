package com.secondserve.controller;

import com.secondserve.docs.AuthDocs;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.LoginRequest;
import com.secondserve.dto.TokenResponseDto;
import com.secondserve.jwt.CookieUtil;
//import com.secondserve.service.AuthService;
//import com.secondserve.service.CustomUserDetailsService;
import com.secondserve.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthDocs {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok().build();
    }
    @PostMapping("/password")
    public ApiResponse<Void> changePassword(@RequestHeader("Authorization") String accessToken, @RequestBody @Valid String newPassword){
        return authService.updatePassword(accessToken, newPassword);
    }

}
