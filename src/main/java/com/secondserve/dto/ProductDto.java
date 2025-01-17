package com.secondserve.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String name;
    private long price;
    private long salePrice;
    private int remain;
    private String prodImage;
}
