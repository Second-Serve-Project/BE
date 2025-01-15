package com.secondserve.service;

import com.secondserve.dto.*;
import com.secondserve.entity.Customer;
import com.secondserve.entity.Product;
import com.secondserve.entity.Receipt;
import com.secondserve.entity.Store;
import com.secondserve.jwt.JwtUtil;
import com.secondserve.repository.ProductRepository;
import com.secondserve.repository.ReceiptRepository;
import com.secondserve.repository.StoreRepository;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.util.CustomerUtil;
import com.secondserve.util.DtoConverter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.sort;

@Service
@RequiredArgsConstructor
public class StoreService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final StoreRepository storeRepository;
    @Autowired
    private final ReceiptRepository receiptRepository;
    @Autowired
    private final CustomerUtil customerUtil;
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
        List<Product> products = productRepository.findByStore(store);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product p : products){
            productDtos.add(DtoConverter.convertToDto(p, ProductDto.class));
        }

        return ApiResponse.fromResultStatus(ResultStatus.PROD_LIST, productDtos);
    }

    public ApiResponse<StoreDto.Spec> getStoreSpec(long storeId) {
        Store store = storeRepository.findById(storeId);
        StoreDto.Spec storeDto = DtoConverter.convertToDto(store, StoreDto.Spec.class);

        // ResultStatus와 데이터를 함께 응답
        return ApiResponse.fromResultStatus(ResultStatus.STORE_SPEC, storeDto);
    }

    public ApiResponse<List<StoreDto.Search>> getStoreList(String search) {
        List<Store> storeList = storeRepository.findByNameContaining(search);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDtoList.add(storeDto);
        }
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }
    public ApiResponse<List<StoreDto.Search>> getStoreCategoryList(String category) {
        List<Store> storeList = storeRepository.findByCategory(category);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDtoList.add(storeDto);
        }
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }
    public ApiResponse<List<StoreDto.Search>> getStoreAddressList(String address) {
        List<Store> storeList = storeRepository.findByAddress(address);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDtoList.add(storeDto);
        }
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }
    public ApiResponse<List<StoreDto.Search>> getStoreXYList(double lat, double lon) {
        List<Store> storeList = storeRepository.findStoresWithin2Km(lat, lon);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDtoList.add(storeDto);
        }
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }
    public ApiResponse<List<StoreDto.Recent>> getRecentStoreList(String accessToken) {
        List<Long> storeIds = paymentService.fetchVisitedStoreList(accessToken);

        // 디버깅용 로그
        System.out.println("Fetched Store IDs: " + storeIds);

        List<Long> uniqueStoreIds = storeIds.stream().distinct().toList();
        System.out.println("Unique Store IDs: " + uniqueStoreIds);

        List<Store> storeList = storeRepository.findStoresWithIds(uniqueStoreIds);

        List<StoreDto.Recent> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Recent storeDto = DtoConverter.convertToDto(store, StoreDto.Recent.class);
            storeDtoList.add(storeDto);
        }
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }

    public ApiResponse<List<StoreDto.Sale>> getSaleStoreList() {
        List<Store> storeList = storeRepository.findStoreWithSaleTime(LocalTime.now());
        List<StoreDto.Sale> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto.Sale storeDto = DtoConverter.convertToDto(store, StoreDto.Sale.class);
            storeDtoList.add(storeDto);
        }

        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
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


        return new StoreDto.XY(lat1,lon1,c*EARTH_RADIUS);
    }

    public ApiResponse<List<StoreDto.Search>> getStoreSortGreenScore2KM(double lat, double lon) {

        List<Store> storeList = storeRepository.findStoresWithin2Km(lat, lon);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        storeList.sort(Comparator.comparingDouble(Store::getGreenScore).reversed());

        for (Store store : storeList) {
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
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
        storeDtoList.sort(Comparator.comparingDouble(StoreDto.XY::getDistance).reversed());
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
        storeDtoList.sort(Comparator.comparingDouble(StoreDto.XY::getDistance).reversed());
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }









}
