package com.example.custom.converter.dto;

import com.example.custom.dto.ConsumptionDto;
import com.example.custom.entity.Consumption;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ConsumptionToDtoConverter implements Converter<Consumption, ConsumptionDto> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public ConsumptionDto convert(Consumption consumption) {
        return ConsumptionDto.builder()
                .productName(consumption.getProductName())
                .productPrice(consumption.getProductPrice())
                .currency(consumption.getCurrency().getCurrencyName())

                .turnoverCount(consumption.getTurnoverCount())
                .unit(consumption.getUnit().getUnitName())
                .arrivalDate(consumption.getArrivalDate().format(dateFormatter))


                .documentNumber(consumption.getDocumentNumber())
                .reportingYearMonth(consumption.getReportingYear() + "-" + consumption.getReportingMonth())


                .storehouseNumber(consumption.getStorehouseNumber())
                .workshopReceiver(consumption.getWorkshopReceiver())
                .build();
    }
}
