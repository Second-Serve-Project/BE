package com.secondserve.docs;

import com.secondserve.dto.ProductDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.StoreDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

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
    ApiResponse<List<StoreDto.Search>> searchStore(@RequestParam String search);

    @Operation(summary = "카테고리별 매장 검색", description = "카테고리를 기준으로 매장을 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 조회 성공")})

    @GetMapping("/category")
    ApiResponse<List<StoreDto.Search>> categoryStore(@RequestParam String category);

    @Operation(summary = "매장 세부 정보", description = "특정 매장의 판매 물품 정보를 가져옵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "매장 상세 정보 조회 성공")})
    @GetMapping("/spec")
    ApiResponse<StoreDto.Spec> specStore(@RequestParam long storeId);

    @Operation(summary = "주소 기반 매장 검색", description = "주소를 기준으로 매장을 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주소 기반 검색 성공")})
    @GetMapping("/search/address")
    ApiResponse<List<StoreDto.Search>> searchByAddress(@RequestParam String address);

    @Operation(summary = "위치 기반 매장 검색", description = "위도와 경도를 기준으로 매장을 검색합니다.")

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "위치 기반 검색 성공")})
    @GetMapping("/search/xy")
    ApiResponse<List<StoreDto.Search>> searchByGPS(@RequestParam double lat, @RequestParam double lon);
    @Operation(summary = "위치 기반 매장 검색", description = "위도와 경도를 기준으로 매장을 검색합니다.")

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "위치 기반 검색 성공")})
    @GetMapping("/search/xy")
    public ApiResponse<List<StoreDto.Recent>> recentStore(@RequestHeader("Authorization") String accessToken);
    @Operation(summary = "위치 기반 매장 검색", description = "위도와 경도를 기준으로 매장을 검색합니다.")

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "위치 기반 검색 성공")})
    @GetMapping("/search/xy")
    ApiResponse<List<StoreDto.Sale>> onSaleStore();
}
