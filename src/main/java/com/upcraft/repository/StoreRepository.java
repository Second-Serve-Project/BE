package com.upcraft.repository;

import com.upcraft.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String name);
    List<Store> findByNameContaining(String name);

}
