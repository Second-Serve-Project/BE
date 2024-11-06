package com.secondserve.service;

import com.secondserve.dto.*;
import com.secondserve.entity.Customer;
import com.secondserve.entity.Product;
import com.secondserve.entity.Receipt;
import com.secondserve.entity.Store;
import com.secondserve.exception.CustomExceptions;
import com.secondserve.repository.ProductRepository;
import com.secondserve.repository.ReceiptRepository;
import com.secondserve.repository.StoreRepository;
import com.secondserve.result.ResultStatus;
import com.secondserve.util.DtoConverter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.metamodel.model.domain.internal.PluralAttributeBuilder.build;

@Service
@AllArgsConstructor
public class StoreService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final StoreRepository storeRepository;
    @Autowired
    private final ReceiptRepository receiptRepository;
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
    /*
    @Transactional
    public ResultStatus modifyProduct(ProductDto productDto) {
        long sellerId = storeRepository.findIdBySellerName(productDto.getSellerId());
        if (!productRepository.existsBySeller_IdAndProductName(sellerId, productDto.getProductName())) {
            throw new CustomExceptions.DuplicateResourceException("Product not found with name: " + productDto.getProductName());
        }
        Product targetProduct = productRepository.findBySeller_IdAndProductName(sellerId, productDto.getProductName());
        targetProduct.setProductName(productDto.getProductName());
        targetProduct.setPrice(productDto.getPrice());
        targetProduct.setRemain(productDto.getRemain());
        productRepository.save(targetProduct);
        return ResultStatus.PROD_MODIFY;
    }*/
//    public DataResponse<ProductDto> listProduct(String name){
//        List<Product> productList= productRepository.findByStore(storeRepository.findByName(name));
//        List<ProductDto> productDtoList = new ArrayList<>();
//        for (Product product : productList){
//            ProductDto productDto = DtoConverter.convertToDto(product, ProductDto.class);
//            productDtoList.add(productDto);
//        }
//        return new DataResponse<>(ResultStatus.PROD_LIST, productDtoList);
//    }

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
    // 결제 성공 후 event로 영수증 생성.
    public void createReceipt(ProductDto productDto, String token){
        Receipt receipt = Receipt.builder()
                .store(productDto.getStore())
                .customer(new Customer("test1"))
                .orderDatetime(LocalDateTime.now())
                .price(productDto.getPrice())
                .build();

        receiptRepository.save(receipt);
    }
}
