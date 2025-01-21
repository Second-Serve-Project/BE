package com.secondserve.service.async;

import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.SearchRequest;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;
import com.secondserve.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreAsyncService {
    private final StoreService storeService;
    private final Executor asyncExecutor;
    private final RedisTemplate<String, Object> redisTemplate;

    @Async("asyncExecutor")
    public CompletableFuture<ApiResponse<List<StoreDto.Search>>> asyncSearchStores(SearchRequest searchRequest) {
        String cacheKey = generateCacheKey(searchRequest);

        // 캐시 확인
        ApiResponse<List<StoreDto.Search>> cachedResponse = getFromCache(cacheKey);
        if (cachedResponse != null) {
            return CompletableFuture.completedFuture(cachedResponse);
        }
        // 비동기 작업
        return CompletableFuture.supplyAsync(() -> {
            ApiResponse<List<StoreDto.Search>> response = storeService.searchStores(searchRequest);

            // 빈 배열은 캐시에 저장하지 않음
            if (response.getData() != null && !response.getData().isEmpty()) {
                saveToCache(cacheKey, response);
            }
            return response;
        }, asyncExecutor);
    }

    private String generateCacheKey(SearchRequest searchRequest) {
        return "storeCache:" + searchRequest.getCategory() +
                (searchRequest.getName() != null ? ":" + searchRequest.getName() : "");
    }

    private ApiResponse<List<StoreDto.Search>> getFromCache(String cacheKey) {
        return (ApiResponse<List<StoreDto.Search>>) redisTemplate.opsForValue().get(cacheKey);
    }

    private void saveToCache(String cacheKey, ApiResponse<List<StoreDto.Search>> response) {
        redisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(10));
    }
}