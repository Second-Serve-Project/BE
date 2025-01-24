package com.secondserve.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    @Nullable
    private String name;

    @Nullable
    private String category;

    @Nullable
    private String address;

    @Nullable
    private Double lat;

    @Nullable
    private Double lon;

    @Nullable
    private String sortBy;
}