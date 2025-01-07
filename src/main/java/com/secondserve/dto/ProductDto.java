package com.secondserve.dto;

import com.secondserve.entity.Store;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
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
