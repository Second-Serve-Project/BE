package com.secondserve.docs;

import com.secondserve.dto.PickUpDto;
import com.secondserve.dto.OrderInfoDto;
import com.secondserve.entity.PickUp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Tag(name = "픽업 관리", description = "픽업 관련 API입니다.")
public interface PickUpDocs {

    @Operation(summary = "픽업 목록 조회", description = "사용자의 픽업 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "픽업 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<List<PickUpDto>> getPickUpList(@RequestHeader("Authorization") String accessToken);

    @Operation(summary = "픽업 저장", description = "새로운 픽업 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "픽업 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    ResponseEntity<PickUp> savePickUp(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody OrderInfoDto orderInfoDto
    );
}
