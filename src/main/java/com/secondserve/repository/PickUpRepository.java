package com.secondserve.repository;

import com.secondserve.entity.PickUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickUpRepository extends JpaRepository<PickUp, Long> {
    @Query("SELECT p FROM PickUp p where p.customerId = :customerId")
    List<PickUp> findAllByCustomerId(@Param("customerId") String customerId);
}
