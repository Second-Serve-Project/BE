package com.secondserve.docs;

import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "인증 관리", description = "인증 관련 API입니다.")
public interface AuthDocs {

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response);
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 업데이트합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ApiResponse<Void> changePassword(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody String newPassword
    );
}
