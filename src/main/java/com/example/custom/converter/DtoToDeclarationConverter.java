package com.example.custom.converter;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Declaration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToDeclarationConverter implements Converter<DeclarationDto, Declaration> {


    @Override
    public Declaration convert(DeclarationDto dto) {
        return Declaration.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .contractNumber(dto.getContractNumber())
                .number(dto.getNumber())
                .importApplicationNumber(dto.getImportApplicationNumber())
                .idCode(dto.getIdCode())
                .contractDate(dto.getContractDate())
                .firmName(dto.getFirmName())
                .isImport(dto.getIsImport())
                .build();
    }
}
