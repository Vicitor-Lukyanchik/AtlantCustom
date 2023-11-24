package com.example.custom.converter;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Declaration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DeclarationToDtoConverter implements Converter<Declaration, DeclarationDto> {

    private static final String EMPTY_STRING = "";

    @Override
    public DeclarationDto convert(Declaration dto) {
        return DeclarationDto.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .contractNumber(dto.getContractNumber())
                .number(dto.getNumber())
                .importApplicationNumber(dto.getImportApplicationNumber())
                .idCode(dto.getIdCode())
                .contractDate(dto.getContractDate())
                .firmName(dto.getFirmName())
                .isImport(dto.getIsImport())
                .message(EMPTY_STRING)
                .build();
    }
}
