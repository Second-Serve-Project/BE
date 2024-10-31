package com.upcraft.repository;

import com.upcraft.entity.Product;
import com.upcraft.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(Store store);

}
