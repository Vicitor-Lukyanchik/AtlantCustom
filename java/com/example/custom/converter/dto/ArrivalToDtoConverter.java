package com.example.custom.converter.dto;

import com.example.custom.dto.ArrivalDto;
import com.example.custom.entity.Arrival;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ArrivalToDtoConverter implements Converter<Arrival, ArrivalDto> {

    private static final String UNDEFINED_DATE = "";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public ArrivalDto convert(Arrival arrival) {
        return ArrivalDto.builder()
                .productName(arrival.getProductName())
                .productPrice(arrival.getProductPrice())
                .currency(arrival.getCurrency().getCurrencyName())
                .turnoverCount(arrival.getTurnoverCount())
                .unit(arrival.getUnit().getUnitName())
                .arrivalDate(getFormatDate(arrival.getArrivalDate()))


                .documentNumber(arrival.getDocumentNumber())
                .reportingYearMonth(arrival.getReportingYear() + "-" + arrival.getReportingMonth())

                .accompanyingDocumentSeries(arrival.getAccompanyingDocumentSeries())
                .accompanyingDocumentNumber(arrival.getAccompanyingDocumentNumber())
                .accompanyingDocumentDate(getFormatDate(arrival.getAccompanyingDocumentDate()))

                .storehouseNumber(arrival.getStorehouseNumber())
                .build();
    }

    private String getFormatDate(LocalDate date) {
        if (date == null) {
            return UNDEFINED_DATE;
        }
        return date.format(dateFormatter);
    }
}
