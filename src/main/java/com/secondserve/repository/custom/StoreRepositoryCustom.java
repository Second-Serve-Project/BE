package com.secondserve.repository.custom;

import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;

import java.time.LocalTime;
import java.util.List;


public interface StoreRepositoryCustom {

    Store findByName(String name);

    StoreDto.Spec findSpecById(long id);

    List<StoreDto.Search> searchStores(String name, String category, String address, Double lat, Double lon);

    List<StoreDto.Recent> findByIds(List<Long> storeIds);

    List<StoreDto.Sale> findBySaleTimeBefore(LocalTime time);
}

