package com.secondserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private String password;

    private String name;

    private String phone;

    private String email;

    private Double greenscore;

    private String grade;

    private String address;

    private LocalDate birthday;

    private Boolean membership;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receipt> receipts;

    public Customer(String customerId){
        this.customerId = customerId;
    }
}