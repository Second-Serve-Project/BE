package com.secondserve.entity;

import com.secondserve.enumeration.PayStatus;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "cart_id")
    private Long cartId;
    @Column(name = "store_id")
    private Long storeId;

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
            final Long storeId,
            final int usedPoint,
            final Customer customer) {

        final PaymentEntity payment = PaymentEntity.builder()
                .impUid(imp_uid)
                .storeId(storeId)
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
