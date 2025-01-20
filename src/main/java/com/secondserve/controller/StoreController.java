package com.secondserve.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondserve.docs.StoreDocs;
import com.secondserve.dto.ProductDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.SearchRequest;
import com.secondserve.kafka.dto.RequestPayload;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.kafka.dto.ResponsePayload;
import com.secondserve.service.StoreService;
import com.secondserve.service.async.StoreAsyncService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/store")
@AllArgsConstructor
@Slf4j
public class StoreController implements StoreDocs {
    private final StoreService storeService;
    private final StoreAsyncService storeAsyncService;
    private final KafkaTemplate<String, String> kafkaTemplate;
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
    public ApiResponse<List<StoreDto.Search>> searchStore(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon
    ){
        return storeService.searchStores(name, category, address, lat, lon);
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
    public CompletableFuture<ApiResponse<List<Store>>> test(){
        return storeAsyncService.asyncTest();
    }

    @GetMapping("/test2")
    public ApiResponse<List<Store>> test2(){
        return storeService.test();
    }


    @GetMapping("/fetch-stores")
    public ApiResponse<List<StoreDto.Search>> fetchStores(@ModelAttribute SearchRequest searchRequest){
        String cacheKey = "category: Category1";

        Object cachedResponse = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResponse != null){
            @SuppressWarnings("unchecked")
            ApiResponse<List<StoreDto.Search>> response = (ApiResponse<List<StoreDto.Search>>) cachedResponse;
            return response;
        }
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<ApiResponse<List<StoreDto.Search>>> future = new CompletableFuture<>();
        responseFutures.put(requestId, (CompletableFuture<ApiResponse<?>>) (CompletableFuture<?>) future);

        RequestPayload requestPayload = new RequestPayload(requestId, searchRequest);
        try{
            kafkaTemplate.send("fetch-store", new ObjectMapper().writeValueAsString(requestPayload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Kafka 메시지 직렬화 실패", e);
        }
        try{
            ApiResponse<List<StoreDto.Search>> response = future.get(10, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(10));
            return response;
        } catch (Exception e){
          responseFutures.remove(requestId);
          throw new RuntimeException("Kafka 응답 대기 실패", e);
        }
    }

    @GetMapping("/fetch-products")
    public ApiResponse<List<ProductDto>> fetchProducts(@RequestParam long storeId) {
        String cacheKey = "store:" + storeId;

        // Redis에서 캐싱된 데이터 확인
        Object cachedResponse = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResponse != null) {
            @SuppressWarnings("unchecked")
            ApiResponse<List<ProductDto>> response = (ApiResponse<List<ProductDto>>) cachedResponse;
            return response;
        }

        // Kafka 요청
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<ApiResponse<List<ProductDto>>> future = new CompletableFuture<>();
        responseFutures.put(requestId, (CompletableFuture<ApiResponse<?>>) (CompletableFuture<?>) future);

        RequestPayload requestPayload = new RequestPayload(requestId, storeId);
        try {
            kafkaTemplate.send("fetch-product", new ObjectMapper().writeValueAsString(requestPayload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Kafka 메시지 직렬화 실패", e);
        }

        try {
            ApiResponse<List<ProductDto>> response = future.get(10, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(10));
            return response;
        } catch (Exception e) {
            responseFutures.remove(requestId);
            throw new RuntimeException("Kafka 응답 대기 실패", e);
        }
    }

    @KafkaListener(topics = "fetch-product-response", groupId = "product-group")
    public void consumeResponseMessage(String response) {
        try {
            // 메시지 역직렬화
            ResponsePayload<ApiResponse<List<ProductDto>>> responsePayload =
                    new ObjectMapper().readValue(response, new TypeReference<>() {});

            // 요청 ID 추출
            String requestId = responsePayload.getRequestId();
            ApiResponse<List<ProductDto>> apiResponse = responsePayload.getResponse();

            // Redis에 데이터 캐싱
            String cacheKey = "store:" + apiResponse.getData().get(0).getName();
            redisTemplate.opsForValue().set(cacheKey, apiResponse, Duration.ofMinutes(10));

            // CompletableFuture에 응답 설정
            CompletableFuture<ApiResponse<?>> future = responseFutures.remove(requestId);
            if (future != null) {
                future.complete(apiResponse);
            } else {
                System.err.println("해당 요청 ID에 대한 Future를 찾을 수 없습니다: " + requestId);
            }
        } catch (JsonProcessingException e) {
            System.err.println("JSON 파싱 실패: " + e.getMessage() + " | 원본 메시지: " + response);
        } catch (Exception e) {
            System.err.println("Kafka 메시지 처리 실패: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "fetch-store-response", groupId = "store-group")
    public void consumeResponseMessage2(String response) {
        try {
            // 메시지 역직렬화
            ResponsePayload<ApiResponse<List<StoreDto.Search>>> responsePayload =
                    new ObjectMapper().readValue(response, new TypeReference<>() {});

            // 요청 ID 추출
            String requestId = responsePayload.getRequestId();
            ApiResponse<List<StoreDto.Search>> apiResponse = responsePayload.getResponse();

            // Redis에 데이터 캐싱
            String cacheKey = "store:" + apiResponse.getData().get(0).getName();
            redisTemplate.opsForValue().set(cacheKey, apiResponse, Duration.ofMinutes(10));

            // CompletableFuture에 응답 설정
            CompletableFuture<ApiResponse<?>> future = responseFutures.remove(requestId);
            if (future != null) {
                future.complete(apiResponse);
            } else {
                System.err.println("해당 요청 ID에 대한 Future를 찾을 수 없습니다: " + requestId);
            }
        } catch (JsonProcessingException e) {
            System.err.println("JSON 파싱 실패: " + e.getMessage() + " | 원본 메시지: " + response);
        } catch (Exception e) {
            System.err.println("Kafka 메시지 처리 실패: " + e.getMessage());
        }
    }
}
