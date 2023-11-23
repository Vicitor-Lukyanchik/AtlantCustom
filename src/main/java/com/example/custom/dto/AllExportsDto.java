package com.example.custom.dto;

import com.example.custom.entity.Export;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllExportsDto {

    private List<ExportDto> exports;

    private String message;
}
