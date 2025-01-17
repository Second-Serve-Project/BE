package com.secondserve.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.ProductDto;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;
import com.secondserve.kafka.dto.RequestPayload;
import com.secondserve.kafka.dto.ResponsePayload;
import com.secondserve.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreListener{
    private final StoreService storeService;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(topics = "fetch-product", groupId = "product-group")
    public void consumeProductMessage(String message) {
        try {
            // 메시지 파싱
            RequestPayload<Long> requestPayload = new ObjectMapper().readValue(message, new TypeReference<>() {});
            long storeId = requestPayload.getRequestParam();

            // 비즈니스 로직 수행
            ApiResponse<List<ProductDto>> responses = storeService.fetchProducts(new Store(storeId));

            // 응답 생성
            ResponsePayload responsePayload = new ResponsePayload(requestPayload.getRequestId(), responses);

            // 응답을 JSON 문자열로 변환
            String jsonResponse = new ObjectMapper().writeValueAsString(responsePayload);

            // Kafka로 JSON 메시지 전송
            kafkaTemplate.send("fetch-product-response", jsonResponse);
        } catch (JsonProcessingException e) {
            System.err.println("Kafka 메시지 직렬화 실패: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
    // TODO
    @KafkaListener(topics = "fetch-store", groupId = "store-group")
    public void consumeStoreMessage(String message) {
        try {
            // 메시지 파싱
            RequestPayload<Object> requestPayload = new ObjectMapper().readValue(message, new TypeReference<>() {});
            //String category = requestPayload.getRequestParam();
            // 비즈니스 로직 수행
            ApiResponse<List<ProductDto>> responses = storeService.fetchProducts(new Store(storeId));

            // 응답 생성
            ResponsePayload responsePayload = new ResponsePayload(requestPayload.getRequestId(), responses);

            // 응답을 JSON 문자열로 변환
            String jsonResponse = new ObjectMapper().writeValueAsString(responsePayload);

            // Kafka로 JSON 메시지 전송
            kafkaTemplate.send("fetch-product-response", jsonResponse);
        } catch (JsonProcessingException e) {
            System.err.println("Kafka 메시지 직렬화 실패: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
}
