package com.upcraft.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;


public class StoreDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Search{
        private String name;
        private String category;
        private String backImage;
        private Integer like;
        private Integer review;
        private String greenScore;
        private String state;
        private LocalTime sale; // 할인 시작 시간
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Spec extends Search{

        private String address;

        private String tel;

        private LocalTime open;

        private LocalTime end;

        private String rest;
    }
}
