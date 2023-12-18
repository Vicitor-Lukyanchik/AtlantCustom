package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ImportByStorehouseDto {

    private Map<String, List<ConsumptionDto>> consumptions;

    private Map<String, List<ArrivalDto>> arrivals;

    private String message;
}
