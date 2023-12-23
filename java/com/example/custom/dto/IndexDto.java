package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class IndexDto {
    private String name;

    private int offset;

    private int pageSize;

    private Map<String, Boolean> sortParameters;
}