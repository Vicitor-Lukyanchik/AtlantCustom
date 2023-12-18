package com.example.custom.converter.dto;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Declaration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DeclarationToDtoConverter implements Converter<Declaration, DeclarationDto> {

    private static final String EMPTY_STRING = "";

    @Override
    public DeclarationDto convert(Declaration declaration) {
        return DeclarationDto.builder()
                .id(declaration.getId())
                .date(declaration.getDate())
                .contractNumber(declaration.getContractNumber())
                .number(declaration.getNumber())
                .importApplicationNumber(declaration.getImportApplicationNumber())
                .idCode(declaration.getIdCode())
                .contractDate(declaration.getContractDate())
                .firmName(declaration.getFirmName())
                .isImport(declaration.getIsImport())
                .message(EMPTY_STRING)
                .build();
    }
}
