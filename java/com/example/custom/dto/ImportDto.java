package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImportDto {

    private List<ArrivalDto> arrivals;
    private List<ConsumptionDto> consumptions;
    private String message;
}
