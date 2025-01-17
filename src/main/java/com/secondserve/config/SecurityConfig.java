package com.secondserve.config;

import com.secondserve.jwt.*;
import com.secondserve.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.secondserve.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.secondserve.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.secondserve.oauth2.service.CustomOAuth2UserService;
import com.secondserve.repository.RefreshRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshRepository refreshRepository;
    private final JwtUtil jwtUtil;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();
        log.info("AuthenticationManager initialized: {}", manager);
        return manager;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        //WebMvcConfig 설정에 따름
        http.cors(Customizer.withDefaults());

        //FormLogin, BasicHttp 비활성화
        http.formLogin((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());
        http
                .authorizeHttpRequests(requests -> requests
                        //.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/oauth2/**","/store/*","/store/**","/login", "/pay/*","/pay/api/order/payment/complete","/signup", "/swagger-ui/**",    // Swagger UI 관련 경로
                                "/v3/api-docs/**","/pickup/*").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(configure ->
                                configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                        .successHandler(oAuth2AuthenticationSuccessHandler)
                                        .failureHandler(oAuth2AuthenticationFailureHandler)
                );

        http
                .addFilterAt(
                        new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        //세션 관리 상태 없음 으로 설정, 서버가 클라이언트의 세션 상태를 유지하지 않음
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }



}