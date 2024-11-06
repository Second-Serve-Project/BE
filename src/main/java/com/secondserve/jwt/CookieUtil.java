package com.secondserve.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);


    public Cookie addCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // HTTPS 요청에만 secure 설정

        return cookie;
    }
    // 테스트 환경에서만 사용.
    private static void logCookieDetails(Cookie cookie) {
        logger.info("Cookie details:");
        logger.info("Name: {}", cookie.getName());
        logger.info("Path: {}", cookie.getPath());
        logger.info("Max Age: {}", cookie.getMaxAge());
        logger.info("Domain: {}", cookie.getDomain());
        logger.info("Secure: {}", cookie.getSecure());
        logger.info("HttpOnly: {}", cookie.isHttpOnly());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Optional.ofNullable(request.getCookies())
                .ifPresent(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> name.equals(cookie.getName()))
                        .forEach(cookie -> {
                            cookie.setValue("");  // 필요하지 않음, setMaxAge(0)으로 충분함
                            cookie.setPath("/");
                            cookie.setMaxAge(0);  // 쿠키 삭제
                            cookie.setHttpOnly(true);
                            cookie.setSecure(request.isSecure());
                            response.addCookie(cookie);
                        }));
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findAny().orElse(null);
    }
}
