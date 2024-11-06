package com.secondserve.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.secondserve.result.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 포함하지 않음
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public static ApiResponse<Void> fromResultStatus(ResultStatus status) {
        return new ApiResponse<>(
                status.getStatusCode(),
                status.getResult(),
                null // metaData는 null
        );
    }

    public static <T> ApiResponse<T> fromResultStatus(ResultStatus status, T data) {
        return new ApiResponse<>(
                status.getStatusCode(),
                status.getResult(),
                data // 메타데이터가 존재하는 경우만 포함
        );
    }

}
