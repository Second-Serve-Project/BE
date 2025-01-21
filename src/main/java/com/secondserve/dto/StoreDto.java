package com.secondserve.dto;

import lombok.*;

import java.time.LocalTime;


public class StoreDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Search{
        private String name;
        private String category;
        private String backImage;
        private int like;
        private int review;
        private double greenScore;
        private String state;
        private String sale; // 할인 시작 시간
        public Search(String name, String category, String backImage, int like, int review, double greenScore,
                      String state, LocalTime sale){
            this.name = name;
            this.category = category;
            this.backImage = backImage;
            this.like = like;
            this.review = review;
            this.greenScore = greenScore;
            this.state = state;
            this.sale = sale.toString();
        }
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

        public Spec(String name, String category, String backImage, int like, int review, double greenScore, String state,
                    LocalTime sale, String address, String tel, LocalTime open, LocalTime end, String rest) {
            super(name, category, backImage, like, review, greenScore, state, sale);
            this.address = address;
            this.tel = tel;
            this.open = open;
            this.end = end;
            this.rest = rest;
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Recent{
        private String name;
        private String backImage;
        private Integer like;
        private String greenScore;
        private String state;
        private LocalTime sale; // 할인 시작 시간
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sale{
        private String name;
        private String backImage;
        private Integer like;
        private double greenScore;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class XY{
        private String name;
        private String backImage;
        private Integer like;
        private double greenScore;
        private double lat;
        private double lon;
        private double distance;
    }
}
