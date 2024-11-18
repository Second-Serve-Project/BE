package com.secondserve.docs;

import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "고객", description = "고객 관련 기능 API입니다.")
public interface CustomerDocs {
    @Operation(summary = "마이페이지", description = "이름, 환경점수, 멤버십 유무, 등급 등을 불러옵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 불러오기 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "토큰 형식 에러"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "토큰 만료 / 비인가 접근"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
    })
    public ApiResponse<CustomerDto.Page> getCustomerInfo(@RequestHeader("Authorization") String token);

}
