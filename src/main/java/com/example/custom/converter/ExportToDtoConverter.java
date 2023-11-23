package com.example.custom.converter;

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
    public ExportDto convert(Export s) {
        return
                ExportDto.builder()
                        .tnved(s.getTnved())
                        .ttnSeries(s.getTtnSeries())
                        .ttnNumber(s.getTtnNumber())
                        .ttnDate(s.getTtnDate().format(dateFormatter))
                        .productName(s.getProductName())
                        .productWeight(s.getProductWeight())
                        .productSum(s.getProductSum())
                        .productCount(s.getProductCount())
                        .unit(s.getUnit().getUnitName())
                        .dateTime(s.getDateTime().format(dateTimeFormatter))
                        .fio(s.getFio())
                        .message(EMPTY_STRING)
                        .build();
    }
}
