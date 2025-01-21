package com.secondserve.service;

import com.secondserve.dto.*;
import com.secondserve.entity.*;
import com.secondserve.repository.ProductRepository;
import com.secondserve.repository.StoreRepository;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.repository.impl.StoreRepositoryCustomImpl;
import com.secondserve.util.DtoConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.sort;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final StoreRepositoryCustomImpl storeRepositoryCustom;
    private final PaymentService paymentService;
    @Transactional
    public ResultStatus registerProduct(ProductDto productDto) {
        long storeId = 1;
        Optional<Product> existingProductOpt = productRepository.findByStoreIdAndName(storeId, productDto.getName());
        if (existingProductOpt.isPresent()) {
            Product product = existingProductOpt.get();
            product.setRemain(product.getRemain() + productDto.getRemain());
            productRepository.save(product);
            return ResultStatus.PROD_REGISTER;
        }
        Product newProduct = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .remain(productDto.getRemain())
                .store(new Store(storeId))
                .build();
        productRepository.save(newProduct);
        return ResultStatus.PROD_REGISTER;
    }

    public ApiResponse<List<ProductDto>> fetchProducts(Store store) {
        return ApiResponse.fromResultStatus(ResultStatus.PROD_LIST, productRepository.findByStore(store));
    }

    public ApiResponse<StoreDto.Spec> getStoreSpec(long storeId) {
        return ApiResponse.fromResultStatus(ResultStatus.STORE_SPEC, storeRepositoryCustom.findSpecById(storeId));
    }

    public ApiResponse<List<StoreDto.Search>> searchStores(SearchRequest searchRequest) {
        List<StoreDto.Search> response = storeRepositoryCustom.searchStores(searchRequest);
        for(StoreDto.Search s : response){
            System.out.println(s.getName());
        }
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeRepositoryCustom.searchStores(searchRequest));
    }


    public ApiResponse<List<StoreDto.Recent>> getRecentStoreList(String accessToken) {
        List<Long> storeIds = paymentService.fetchVisitedStoreList(accessToken);
        List<Long> uniqueStoreIds = storeIds.stream().distinct().toList();

        List<StoreDto.Recent> storeList = storeRepositoryCustom.findByIds(uniqueStoreIds);

        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeList);
    }

    public ApiResponse<List<StoreDto.Sale>> getSaleStoreList() {
        List<StoreDto.Sale> storeList = storeRepositoryCustom.findBySaleTimeBefore(LocalTime.now());

        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeList);
    }
    public ApiResponse<List<Store>> test() {
        List<Store> storeList = storeRepository.findAll();
        List<StoreDto.Sale> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Sale storeDto = DtoConverter.convertToDto(store, StoreDto.Sale.class);
            storeDtoList.add(storeDto);
        }

        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeList);
    }

    public StoreDto.XY calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double EARTH_RADIUS = 6371.0;

        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);


        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Store store=storeRepository.findStoresByLatAndLon(lat1,lon1).get();


        return new StoreDto.XY(store.getName(),store.getBackImage(),store.getLike(),store.getGreenScore(),lat1,lon1,c*EARTH_RADIUS);
    }

    public ApiResponse<List<StoreDto.Search>> getStoreSortGreenScore2KM(double lat, double lon) {

        List<Store> storeList = storeRepository.findStoresWithin2Km(lat, lon);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        storeList.sort(Comparator.comparingDouble(Store::getGreenScore).reversed());

        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDto.setGreenScore(Double.toString(store.getGreenScore()));
            storeDtoList.add(storeDto);
        }

        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }

    public ApiResponse<List<StoreDto.Search>> getStoreSortGreenScore() {

        List<Store> storeList = storeRepository.findAll();
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        storeList.sort(Comparator.comparingDouble(Store::getGreenScore).reversed());

        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDto.setGreenScore(Double.toString(store.getGreenScore()));
            storeDtoList.add(storeDto);
        }

        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }
    public ApiResponse<List<StoreDto.XY>> getStoreSortDistance(double lat, double lon) {

        List<Store> storeList = storeRepository.findAll();

        List<StoreDto.XY> storeDtoList = new ArrayList<>();
        for (Store store : storeList)
        {
            StoreDto.XY storeDto = DtoConverter.convertToDto(store, StoreDto.XY.class);
            storeDto=calculateDistance(storeDto.getLat(),storeDto.getLon(),lat,lon);
            storeDtoList.add(storeDto);
        }
        storeDtoList.sort(Comparator.comparingDouble(StoreDto.XY::getDistance));
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }
    public ApiResponse<List<StoreDto.XY>> getStoreSortDistance2KM(double lat, double lon) {

        List<Store> storeList = storeRepository.findStoresWithin2Km(lat,lon);

        List<StoreDto.XY> storeDtoList = new ArrayList<>();
        for (Store store : storeList)
        {
            StoreDto.XY storeDto = DtoConverter.convertToDto(store, StoreDto.XY.class);
            storeDto=calculateDistance(storeDto.getLat(),storeDto.getLon(),lat,lon);
            storeDtoList.add(storeDto);
        }
        storeDtoList.sort(Comparator.comparingDouble(StoreDto.XY::getDistance));
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }









}
