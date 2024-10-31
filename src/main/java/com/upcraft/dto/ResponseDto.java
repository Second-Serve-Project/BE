package com.upcraft.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.upcraft.result.ResultStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 포함하지 않음
public class ResponseDto<U> {
    private int statusCode;
    private String message;
    private U data;

    public static ResponseDto<Void> fromResultStatus(ResultStatus status) {
        return new ResponseDto<>(
                status.getStatusCode(),
                status.getResult(),
                null // metaData는 null
        );
    }

    public static <U> ResponseDto<U> fromResultStatusWithData(ResultStatus status, U data) {
        return new ResponseDto<>(
                status.getStatusCode(),
                status.getResult(),
                data // 메타데이터가 존재하는 경우만 포함
        );
    }

}
