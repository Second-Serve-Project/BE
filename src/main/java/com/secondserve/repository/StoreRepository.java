package com.secondserve.repository;

import com.secondserve.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String name);
    Store findById(long id);
    List<Store> findByNameContaining(String name);
    List<Store> findByCategory(String category);
    List<Store> findByAddress(String address);
    @Query(
            value = "SELECT * FROM Store s WHERE ST_Distance_Sphere(point(s.lon, s.lat), point(:lon, :lat)) <= 2000",
            nativeQuery = true
    )
    List<Store> findStoresWithin2Km(@Param("lat") double lat, @Param("lon") double lon);

    @Query(value = "SELECT s FROM Store s WHERE s.id IN :storeIds")
    List<Store> findStoresWithIds(@Param("storeIds") List<Long> ids);
    @Query(value = "SELECT s FROM Store s WHERE s.sale <= :sale")
    List<Store> findStoreWithSaleTime(@Param("sale") LocalTime time);


}
