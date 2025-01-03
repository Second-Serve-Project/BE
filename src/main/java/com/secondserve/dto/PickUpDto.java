package com.secondserve.dto;

import com.secondserve.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PickUpDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private double lat;
    private double lon;
    private String address;
    private List<Cart> cart;
}
