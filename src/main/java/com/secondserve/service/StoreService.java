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
import java.util.List;
import java.util.Optional;

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

    public ApiResponse<List<StoreDto.Search>> searchStores(String name, String category, String address, Double lat, Double lon) {
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_STORE_SEARCH, storeRepositoryCustom.searchStores(name, category, address, lat, lon));
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



}
