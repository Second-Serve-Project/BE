package com.upcraft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 매장 ID

    private String name; // 매장명

    private String category; // 매장 구분

    private String address; // 주소

    private String backImage; // 가게 대표사진 (TEXT 또는 BLOB)

    private String tel; // 매장 전화번호

    
    private Double greenScore; // 환경 점수

    
    private Integer like; // 찜수 (기본값 0)

    
    private Integer review; // 리뷰수 (기본값 0)

    
    private LocalTime open; // 오픈 시간

    
    private LocalTime end; // 종료 시간

    
    private LocalTime sale; // 할인 시작 시간

    private String rest; // 휴무일

    private String state; // 운영 상태

    
    private Double lat; // 위도

    
    private Double lon; // 경도

    
    private Boolean ad; // 광고 유무 (기본값 false)
}