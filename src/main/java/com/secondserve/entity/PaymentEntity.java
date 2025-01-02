package com.secondserve.entity;

import com.secondserve.dto.CartDto;
import com.secondserve.enumeration.PayStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imp_uid;

    private Long cartId;

    private int usedPoint;
    private PayStatus payStatus;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public static PaymentEntity createPaymentEntity(
            final String imp_uid,
            final Long cartId,
            final int usedPoint,
            final Customer customer) {

        final PaymentEntity payment = PaymentEntity.builder()
                .imp_uid(imp_uid)
                .cartId(cartId)
                .usedPoint(usedPoint)
                .customer(customer)
                .payStatus(PayStatus.COMPLETED)
                .build();

        return payment;
    }

    public void refund() {
        this.payStatus = PayStatus.REFUND;
    }
}
