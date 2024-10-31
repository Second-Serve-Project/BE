package com.upcraft.docs;

import com.upcraft.dto.CustomerDto;
import com.upcraft.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "회원가입", description = "회원가입 관련 API입니다.")
public interface RegisterDocs {

    @Operation(summary = "회원가입", description = "유저 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
    public ResponseDto<Void> signUp(@RequestBody CustomerDto.Register params);


}