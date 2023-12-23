package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ArrivalDto {

    private String storehouseNumber;
    private Integer documentNumber;
    private String arrivalDate;

    private String accompanyingDocumentNumber;
    private String accompanyingDocumentSeries;
    private String accompanyingDocumentDate;

    private String productName;
    private BigDecimal productPrice;
    private String currency;
    private Double turnoverCount;
    private String unit;
    private String reportingYearMonth;
}
