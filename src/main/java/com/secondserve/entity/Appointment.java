package com.secondserve.entity;

import com.secondserve.enumeration.PayStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @Builder
    public static Appointment buildAppointment(
            final Customer customer,
            final PayStatus payStatus
    ){

        return Appointment.builder()
                .customer(customer)
                .payStatus(payStatus)
                .build();
    }

    public void payment() throws IllegalAccessException {
        if (this.payStatus == PayStatus.COMPLETED) {
            throw new IllegalAccessException();
        }

        this.payStatus = PayStatus.COMPLETED;
    }

}
