package com.secondserve.kafka.dto;

import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePayload<T> {
    private String requestId;
    private T response;
}