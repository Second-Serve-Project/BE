package com.secondserve.repository;

import com.secondserve.entity.PaymentEntity;
import com.siot.IamportRestClient.response.Payment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("SELECT p FROM PaymentEntity p WHERE p.imp_uid = :imp_uid")
    PaymentEntity findByImpUid(@Param("imp_uid") String imp_uid);
    void delete(@NotNull PaymentEntity payment);
}
