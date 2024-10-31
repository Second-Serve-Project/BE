package com.upcraft.service;

import com.upcraft.dto.DataResponse;
import com.upcraft.dto.ProductDto;
import com.upcraft.dto.StoreDto;
import com.upcraft.entity.Product;
import com.upcraft.entity.Store;
import com.upcraft.exception.CustomExceptions;
import com.upcraft.repository.ProductRepository;
import com.upcraft.repository.StoreRepository;
import com.upcraft.result.ResultStatus;
import com.upcraft.util.DtoConverter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.LongStream.builder;
import static org.hibernate.metamodel.model.domain.internal.PluralAttributeBuilder.build;

@Service
@AllArgsConstructor
public class StoreService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final StoreRepository storeRepository;

    /*@Transactional
    public ResultStatus registerProduct(ProductDto.register productDto) {
        long storeId = storeRepository.findIdByName(storeDto.get());
        if (productRepository.existsBySidAndProductName(storeId, productDto.getProductName())) {
            throw new CustomExceptions.DuplicateResourceException("Product already exists with name: " + productDto.getProductName());
        }
        Product newProduct = Product.builder()
                .name(productDto.getProductName())
                .price(productDto.getPrice())
                .remain(productDto.getRemain())
                .store(new Store(productDto.getSellerId()))
                .build();
        productRepository.save(newProduct);
        return ResultStatus.PROD_REGISTER;
    }

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
    public DataResponse<ProductDto> listProduct(String name){
        List<Product> productList= productRepository.findByStore(storeRepository.findByName(name));
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : productList){
            ProductDto productDto = DtoConverter.convertToDto(product, ProductDto.class);
            productDtoList.add(productDto);
        }
        return new DataResponse<>(ResultStatus.PROD_LIST, productDtoList);
    }

    public DataResponse<ProductDto> refreshPackage(String name) {
        // 매장 이름을 기반으로 제품 리스트를 가져옴
        List<Product> productList = productRepository.findByStore(storeRepository.findByName(name));

        // 가져온 제품 리스트를 DTO로 변환
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : productList) {
            ProductDto productDto = DtoConverter.convertToDto(product, ProductDto.class);
            productDtoList.add(productDto);
        }

        // 제품 DTO 리스트를 무작위로 섞고 6개를 추출
        Collections.shuffle(productDtoList); // 리스트 섞기
        List<ProductDto> randomProductDtoList = productDtoList.stream()
                .limit(6) // 6개만 선택
                .collect(Collectors.toList()); // 별도의 리스트로 수집

        // 새로 선택된 6개의 제품 리스트 반환
        return new DataResponse<>(ResultStatus.PROD_LIST, randomProductDtoList);
    }
    public DataResponse<StoreDto.Search> getStoreList(String search){
        List<Store> storeList = storeRepository.findByNameContaining(search);
        List<StoreDto.Search> storeDtoList = new ArrayList<>();
        for (Store store : storeList){
            StoreDto.Search storeDto = DtoConverter.convertToDto(store, StoreDto.Search.class);
            storeDtoList.add(storeDto);
        }
        return new DataResponse<>(ResultStatus.SUCCESS_STORE_SEARCH, storeDtoList);
    }

}
