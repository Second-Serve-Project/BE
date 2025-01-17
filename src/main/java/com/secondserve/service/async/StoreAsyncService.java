package com.secondserve.service.async;

import com.secondserve.dto.ApiResponse;
import com.secondserve.entity.Store;
import com.secondserve.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class StoreAsyncService {
    private final StoreService storeService;
    private final Executor asyncExecutor;

    @Async("asyncExecutor")
    public CompletableFuture<ApiResponse<List<Store>>> asyncTest() {
        return CompletableFuture.supplyAsync(() -> storeService.test(), asyncExecutor);
    }

}
