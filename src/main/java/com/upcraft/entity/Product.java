package com.upcraft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;  // 상품 id

    private String name;  // 상품 이름

    private int price;  // 가격

    private int remain;  // 남은 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sid", nullable = false)  // 매장 id (외래키)
    private Store store;  // 매장과의 다대일 관계

    private double weight;  // 무게
    @Lob
    private String prodImage;
}
