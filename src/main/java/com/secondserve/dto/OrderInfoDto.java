package com.secondserve.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderInfoDto {
    private int usedPoint;         // 사용된 포인트
    private String impUid;         // 아임포트 UID
    private String payMethod;      // 결제 방법 (카드 등)
    private String orderNum;       // 주문 번호
    private String name;           // 상품 이름
    private String storeName;
    private int amount;            // 결제 금액
    private String phone;          // 구매자 연락처
    private String deliveryAddress1; // 우편번호
    private String deliveryAddress2; // 주소 1
    private String deliveryAddress3; // 주소 2
    private List<CartDto> cart;
}
