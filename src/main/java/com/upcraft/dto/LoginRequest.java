package com.upcraft.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String password;

    private String userType;
}
