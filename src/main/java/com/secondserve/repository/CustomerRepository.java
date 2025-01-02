package com.secondserve.repository;

import com.secondserve.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCustomerId(String customerId);
    Customer findByCustomerId(String customerId);
}
