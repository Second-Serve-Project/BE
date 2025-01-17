package com.secondserve.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestPayload<T> {
    private String requestId;
    private T requestParam;
}