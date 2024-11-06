package com.secondserve.repository;

import com.secondserve.entity.Product;
import com.secondserve.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(Store store);
    Optional<Product> findByStoreIdAndName(long sid, String name);

}
