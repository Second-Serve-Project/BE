package com.secondserve.repository;

import com.secondserve.dto.ProductDto;
import com.secondserve.entity.Product;
import com.secondserve.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.secondserve.dto.ProductDto(p.name, p.price, p.salePrice, p.remain, p.prodImage) FROM Product p WHERE p.store = :store")
    List<ProductDto> findByStore(@Param("store") Store store);
    Optional<Product> findByStoreIdAndName(long sid, String name);

}
