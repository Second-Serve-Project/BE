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

    private Double greenScore;

    private String grade;

    private String address;

    private LocalDate birthday;

    private Boolean membership;
    private Long point;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receipt> receipts;

    @OneToMany
    private List<PaymentEntity> payments;

    @OneToMany
    private List<PickUp> pickUps;

    public Customer(String customerId, String email){
        this.customerId = customerId;
        this.email = email;
    }
}