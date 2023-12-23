package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DeclarationDto {

    private Long id;

    private Long idCode;

    private Boolean isImport;

    private LocalDate date;

    private String number;

    private String importApplicationNumber;

    private String contractNumber;

    private LocalDate contractDate;

    private String firmName;

    private String message;
}
