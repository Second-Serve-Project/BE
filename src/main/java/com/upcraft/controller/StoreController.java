package com.upcraft.controller;

import com.upcraft.dto.DataResponse;
import com.upcraft.dto.ProductDto;
import com.upcraft.dto.ResponseDto;
import com.upcraft.dto.StoreDto;
import com.upcraft.result.ResultStatus;
import com.upcraft.service.StoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/m")
@AllArgsConstructor
public class StoreController {
    @Autowired
    private final StoreService storeService;
    /*@PostMapping("/new")
    public ResponseDto<Void> registerProduct(@RequestHeader("Authorization") String token,
                                             @Valid @RequestBody ProductDto.register productDto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseDto.fromResultStatus(ResultStatus.BAD_REQUEST);
        }
        ResultStatus registerResult = storeService.registerProduct(productDto);
        return ResponseDto.fromResultStatus(registerResult);
    }*/
    @GetMapping("/search")
    public ResponseDto<List<StoreDto.Search>> searchStore(@RequestParam String search){
        DataResponse<StoreDto.Search> searchResult = storeService.getStoreList(search);
        return ResponseDto.fromResultStatusWithData(searchResult.getResultStatus(), searchResult.getData());
    }
    @GetMapping("/list") // 특정 매장 페이지로 이동 후 판매하는 물품 리스트.
    public ResponseDto<List<ProductDto>> listProduct(@RequestParam String name){
        DataResponse<ProductDto> listResult = storeService.listProduct(name);
        return ResponseDto.fromResultStatusWithData(listResult.getResultStatus(), listResult.getData());
    }
    @GetMapping("/random")
    public ResponseDto<List<ProductDto>> refreshPackage(@RequestParam String name){
        DataResponse<ProductDto> packageResult = storeService.refreshPackage(name);
        return ResponseDto.fromResultStatusWithData(packageResult.getResultStatus(), packageResult.getData());
    }
}
