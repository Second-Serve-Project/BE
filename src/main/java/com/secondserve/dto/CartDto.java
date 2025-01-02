package com.secondserve.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private String merchant_name;
    private int merchant_num;
}
