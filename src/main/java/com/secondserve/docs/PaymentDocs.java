package com.secondserve.docs;

import com.secondserve.dto.OrderInfoDto;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "결제", description = "결제 관련 API입니다.")
public interface PaymentDocs {


    @Operation(summary = "결제 완료 처리", description = "결제 완료 후 유효성을 검증하고 데이터를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 완료 성공"),
            @ApiResponse(responseCode = "400", description = "결제 처리 중 오류 발생"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/api/order/payment/complete")
    ResponseEntity<String> paymentComplete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody OrderInfoDto orderInfo) throws IOException;
}
