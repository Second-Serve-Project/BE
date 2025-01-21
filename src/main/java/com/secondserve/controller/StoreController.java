package com.secondserve.controller;

import com.secondserve.docs.StoreDocs;
import com.secondserve.dto.CustomerDto;
import com.secondserve.dto.ProductDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.Store;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.service.StoreService;
import com.secondserve.service.async.StoreAsyncService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/store")
@AllArgsConstructor
@Slf4j
public class StoreController implements StoreDocs {
    @Autowired
    private final StoreService storeService;
    private final StoreAsyncService storeAsyncService;
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

    @GetMapping("/distance")
    @ResponseBody
    public ResponseEntity<StoreDto.XY> getDistance(@RequestParam double lat1, @RequestParam double lon1,
                                                   @RequestParam double lat2, @RequestParam double lon2)
    {

        // 거리 계산
        return ResponseEntity.ok(storeService.calculateDistance(lat1, lon1, lat2, lon2));
    }
    //2km 이내 거리에 있는 가게 greenScore 별 정렬
    @GetMapping("/sort/greenScore2KM")
    public ApiResponse<List<StoreDto.Search>> getSortStoreGreenScore2KM(@RequestParam double lat, @RequestParam double lon){
        return storeService.getStoreSortGreenScore2KM(lat,lon);
    }
    //모든 거리에 있는 가게 greenScore 별 정렬
    @GetMapping("/sort/greenScore")
    public ApiResponse<List<StoreDto.Search>> getSortStoreGreenScore(){
        return storeService.getStoreSortGreenScore();
    }


    @GetMapping("/sort/distance")
    public ApiResponse<List<StoreDto.XY>> getSortStoreDistance(@RequestParam double lat, @RequestParam double lon){
        return storeService.getStoreSortDistance(lat,lon);
    }
    @GetMapping("/sort/distance2KM")
    public ApiResponse<List<StoreDto.XY>> getSortStoreDistance2KM(@RequestParam double lat, @RequestParam double lon){
        return storeService.getStoreSortDistance2KM(lat,lon);
    }

}
