package com.secondserve.repository;

import com.secondserve.entity.PaymentEntity;
import com.siot.IamportRestClient.response.Payment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("SELECT p FROM PaymentEntity p WHERE p.impUid = :imp_uid")
    PaymentEntity findByImpUid(@Param("imp_uid") String imp_uid);

    @Query(value = "SELECT p.storeId FROM PaymentEntity p WHERE p.customer.id = :customer_id")
    List<Long> findStoreIdByCustomerId(@Param("customer_id") long customerId);
    void delete(@NotNull PaymentEntity payment);
}
