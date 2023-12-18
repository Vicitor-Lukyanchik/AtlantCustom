package com.example.custom.converter.dto;

import com.example.custom.dto.ExportDto;
import com.example.custom.entity.Export;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ExportToDtoConverter implements Converter<Export, ExportDto> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String EMPTY_STRING = "";

    @Override
    public ExportDto convert(Export export) {
        return
                ExportDto.builder()
                        .tnved(export.getTnved())
                        .ttnSeries(export.getTtnSeries())
                        .ttnNumber(export.getTtnNumber())
                        .ttnDate(export.getTtnDate().format(dateFormatter))
                        .productName(export.getProductName())
                        .productWeight(export.getProductWeight())
                        .productSum(export.getProductSum())
                        .productCount(export.getProductCount())
                        .unit(export.getUnit().getUnitName())
                        .dateTime(export.getDateTime().format(dateTimeFormatter))
                        .fio(export.getFio())
                        .message(EMPTY_STRING)
                        .build();
    }
}
