package com.secondserve.repository.custom;

import com.secondserve.dto.SearchRequest;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;

import java.time.LocalTime;
import java.util.List;


public interface StoreRepositoryCustom {

    Store findByName(String name);

    StoreDto.Spec findSpecById(long id);

    List<StoreDto.Search> searchStores(SearchRequest searchRequest);

    List<StoreDto.Recent> findByIds(List<Long> storeIds);

    List<StoreDto.Sale> findBySaleTimeBefore(LocalTime time);
}

