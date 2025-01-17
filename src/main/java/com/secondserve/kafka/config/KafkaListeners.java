package com.secondserve.kafka.config;

import com.secondserve.service.StoreService;
import com.secondserve.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final StoreService storeService;

    @KafkaListener(topics = "fetch-product", groupId = "product-group")
    public void consumeProductMessage(Store store) {
        // 메시지 역직렬화 및 로직 처리
        storeService.fetchProducts(store);
    }
    @KafkaListener(topics = "fetch-store", groupId = "store-group")
    public void consumeStoreMessage(String name, String category, String address, Double lat, Double lon) {
            // 메시지 역직렬화 및 로직 처리
            storeService.searchStores(name, category, address, lat, lon);
    }

}
