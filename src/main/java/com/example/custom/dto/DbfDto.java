package com.example.custom.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class DbfDto {

    private List<String> columns;

    private List<List<String>> objects;
}
