package com.secondserve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String name;
    private String category;
    private String address;
    private Double lat;
    private Double lon;
}