package com.secondserve.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page {
        private String name;
        private double greenScore;
        private String grade;
        private Boolean membership;
    }
}
