package com.example.custom.converter.dto;

import com.example.custom.dto.*;
import com.example.custom.entity.Declaration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ImportToByStorehouseConverter implements Converter<ImportDto, ImportByStorehouseDto> {


    @Override
    public ImportByStorehouseDto convert(ImportDto source) {
        return ImportByStorehouseDto.builder()
                .arrivals(convertArrival(source.getArrivals()))
                .consumptions(convertConsumption(source.getConsumptions()))
                .message(source.getMessage())
                .build();
    }

    private Map<String, List<ArrivalDto>> convertArrival(List<ArrivalDto> arrivals) {
        Map<String, List<ArrivalDto>> result = new HashMap<>();

        for (ArrivalDto arrival : arrivals) {
            String storehouseNumber = arrival.getStorehouseNumber();
            if (result.containsKey(storehouseNumber)) {
                result.get(storehouseNumber).add(arrival);
            } else {
                List<ArrivalDto> valueResult = new ArrayList<>();
                valueResult.add(arrival);
                result.put(storehouseNumber, valueResult);
            }
        }
        return result;
    }

    private Map<String, List<ConsumptionDto>> convertConsumption(List<ConsumptionDto> consumptions) {
        Map<String, List<ConsumptionDto>> result = new HashMap<>();

        for (ConsumptionDto consumption : consumptions) {
            String storehouseNumber = consumption.getStorehouseNumber();
            if (result.containsKey(storehouseNumber)) {
                result.get(storehouseNumber).add(consumption);
            } else {
                List<ConsumptionDto> valueResult = new ArrayList<>();
                valueResult.add(consumption);
                result.put(storehouseNumber, valueResult);
            }
        }
        return result;
    }
}
