package com.secondserve.docs;

import com.secondserve.dto.CustomerDto;
import com.secondserve.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "회원가입", description = "회원가입 관련 API입니다.")
public interface RegisterDocs {

    @Operation(summary = "아이디 중복확인", description = "회원가입 도중 아이디 중복 확인.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
    public Boolean checkIdAvailability(@RequestParam String id);
    @Operation(summary = "회원가입", description = "유저 정보를 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
    public ApiResponse<Void> signUp(@RequestBody CustomerDto.Register params);

}