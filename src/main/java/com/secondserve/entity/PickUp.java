package com.secondserve.entity;

import com.secondserve.dto.OrderInfoDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pickup")
public class PickUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private double lat;
    private double lon;
    private String name;
    private Long cartId;
    private String address;
    private String customerId;

    public PickUp createPickUp(Store store, Cart cart, Customer customer){
           return PickUp.builder()
                   .startTime(LocalTime.now())
                   .endTime(LocalTime.of(0,30))
                   .name(store.getName())
                   .lat(store.getLat())
                   .lon(store.getLon())
                   .cartId(cart.getCartId())
                   .address(store.getAddress())
                   .customerId(customer.getCustomerId())
                   .build();
    }
}
