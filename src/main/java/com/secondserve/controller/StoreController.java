package com.secondserve.controller;

import com.secondserve.docs.StoreDocs;
import com.secondserve.dto.ProductDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.SearchRequest;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.service.StoreService;
import com.secondserve.service.async.StoreAsyncService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/store")
@AllArgsConstructor
@Slf4j
public class StoreController implements StoreDocs {
    private final StoreService storeService;
    private final StoreAsyncService storeAsyncService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<String, CompletableFuture<ApiResponse<?>>> responseFutures = new ConcurrentHashMap<>();


    @PostMapping("/new")
    public ResponseEntity<ApiResponse<Void>> registerProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult bindingResult) {

        // 입력값 검증 오류가 있는 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(ResultStatus.BAD_REQUEST.getStatusCode())  // 상태 코드 설정
                    .body(ApiResponse.fromResultStatus(ResultStatus.BAD_REQUEST));  // 본문에 커스텀 응답 객체 포함
        }
        ResultStatus registerResult = storeService.registerProduct(productDto);

        return ResponseEntity
                .status(registerResult.getStatusCode())  // 상태 코드 설정
                .body(ApiResponse.fromResultStatus(registerResult));  // 본문에 커스텀 응답 객체 포함
    }

    @GetMapping("/search")
    public ApiResponse<List<StoreDto.Search>> searchStore(@ModelAttribute SearchRequest searchRequest){
        return storeService.searchStores(searchRequest);
    }
    @GetMapping("/spec") // 특정 매장 페이지로 이동 후 판매하는 물품 리스트.
    public ApiResponse<StoreDto.Spec> specStore(@RequestParam long storeId) {
        return storeService.getStoreSpec(storeId);
    }
    @GetMapping("/products") // 특정 매장 페이지로 이동 후 판매하는 물품 리스트.
    public ApiResponse<List<ProductDto>> getProducts(@RequestParam long storeId) {
        return storeService.fetchProducts(new Store(storeId));
    }
    @GetMapping("/recent")
    public ApiResponse<List<StoreDto.Recent>> recentStore(@RequestHeader("Authorization") String accessToken){
        return storeService.getRecentStoreList(accessToken);
    }
    @GetMapping("/sale")
    public ApiResponse<List<StoreDto.Sale>> onSaleStore(){
        return storeService.getSaleStoreList();
    }
    @GetMapping("/test")
    public CompletableFuture<ApiResponse<List<StoreDto.Search>>> test(@ModelAttribute SearchRequest searchRequest){
        return storeAsyncService.asyncSearchStores(searchRequest);
    }

    @GetMapping("/test2")
    public ApiResponse<List<Store>> test2(){
        return storeService.test();
    }

}
