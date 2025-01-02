package com.secondserve.repository;

import com.secondserve.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT MAX(c.cartId) FROM Cart c")
    Optional<Long> findMaxCartId();

}
