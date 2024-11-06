package com.secondserve.controller;

import com.secondserve.dto.ProductDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.StoreDto;
import com.secondserve.result.ResultStatus;
import com.secondserve.service.StoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/store")
@AllArgsConstructor
public class StoreController {
    @Autowired
    private final StoreService storeService;
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
    public ApiResponse<List<StoreDto.Search>> searchStore(@RequestParam String search){
        return storeService.getStoreList(search);
    }
    @GetMapping("/category")
    public ApiResponse<List<StoreDto.Search>> categoryStore(@RequestParam String category){
        return storeService.getStoreCategoryList(category);
    }
    @GetMapping("/spec") // 특정 매장 페이지로 이동 후 판매하는 물품 리스트.
    public ApiResponse<StoreDto.Spec> specStore(@RequestParam long storeId) {
        return storeService.getStoreSpec(storeId);
    }
    @GetMapping("/search/address")
    public ApiResponse<List<StoreDto.Search>> onSaleStore(@RequestParam String address){
        return storeService.getStoreAddressList(address);
    }
    @GetMapping("/search/xy")
    public ApiResponse<List<StoreDto.Search>> onSaleStore(@RequestParam double lat, @RequestParam double lon){
        return storeService.getStoreXYList(lat, lon);
    }

}
