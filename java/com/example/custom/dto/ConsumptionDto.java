package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConsumptionDto {

    private String storehouseNumber;
    private String workshopReceiver;

    private Integer documentNumber;
    private String arrivalDate;

    private String productName;
    private BigDecimal productPrice;
    private String currency;
    private Double turnoverCount;
    private String unit;
    private String reportingYearMonth;
}
