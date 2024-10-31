package com.upcraft.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtConstants {
    HEADER_AUTHORIZATION("Authorization"),
    TOKEN_PREFIX("Bearer "),
    REFRESH_TOKEN_COOKIE_NAME("refresh_token");

    private final String value;

    @Override
    public String toString(){
        return this.value;
    }
}