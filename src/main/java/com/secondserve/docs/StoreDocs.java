package com.secondserve.docs;

import com.secondserve.dto.ProductDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.SearchRequest;
import com.secondserve.dto.StoreDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "매장", description = "매장 관련 API입니다.")
public interface StoreDocs {

    @Operation(summary = "상품 등록", description = "새로운 상품을 매장에 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값 검증 실패") })
    @PostMapping("/new")
    ResponseEntity<ApiResponse<Void>> registerProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult bindingResult);

    @Operation(summary = "매장 검색", description = "매장 이름으로 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "매장명 조회 성공")})
    @GetMapping("/search")
    public ApiResponse<List<StoreDto.Search>> searchStore(@ModelAttribute SearchRequest searchRequest);


    @Operation(summary = "매장 세부 정보", description = "특정 매장의 판매 물품 정보를 가져옵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "매장 상세 정보 조회 성공")})
    @GetMapping("/spec")
    ApiResponse<StoreDto.Spec> specStore(@RequestParam long storeId);


    @Operation(summary = "최근 주문 매장 검색", description = "최근 주문했던 매장을 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반환 성공")})
    @GetMapping("/recent")
    ApiResponse<List<StoreDto.Recent>> recentStore(@RequestHeader("Authorization") String accessToken);
    @Operation(summary = "세일 중인 매장 검색", description = "현재 세일 중인 매장을 검색합니다.")

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반환 성공")})
    @GetMapping("/sale")
    ApiResponse<List<StoreDto.Sale>> onSaleStore();
}
