package com.secondserve.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CustomerDto {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Register {
        @NotBlank
        private String id;
        @NotBlank
        private String password;
    }
}
