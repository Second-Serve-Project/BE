package com.secondserve.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String password;

    private String userType;
}
