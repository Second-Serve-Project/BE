package com.upcraft.dto;

import com.upcraft.result.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DataResponse<T> {
    private final ResultStatus resultStatus;
    private final List<T> data;
}
