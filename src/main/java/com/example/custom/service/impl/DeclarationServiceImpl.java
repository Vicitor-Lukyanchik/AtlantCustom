package com.example.custom.service.impl;

import com.example.custom.converter.DbfToDeclarationConverter;
import com.example.custom.converter.DeclarationToDtoConverter;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Declaration;
import com.example.custom.exception.ServiceException;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.DeclarationRepository;
import com.example.custom.service.DeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeclarationServiceImpl implements DeclarationService {

    private static final String DECLARATION_DBF_PATH = "D:/GarbageExamples/BAZ_GTD.DBF";
    private static final String DECLARATION_NOT_FOUND_MESSAGE = "Не найдена декларация с номером ";

    private final DbfToDeclarationConverter dbfToDeclarationConverter;
    private final DbfDosEncodingFileReader dbfReader;
    private final DeclarationRepository declarationRepository;
    private final DeclarationToDtoConverter declarationToDtoConverter;

    @Override
    public List<Declaration> importDeclarationsFromDbf() {
        DbfDto dbfDto = dbfReader.read(DECLARATION_DBF_PATH);
        List<Declaration> declarations = dbfToDeclarationConverter.convert(dbfDto);
        return saveDeclarations(declarations);
    }

    private List<Declaration> saveDeclarations(List<Declaration> declarations) {
        return declarationRepository.saveAll(declarations);
    }

    @Override
    public Declaration findByIdCode(Long idCode) {
        Optional<Declaration> result = declarationRepository.findByIdCode(idCode);
        if (result.isEmpty()){
            throw new ServiceException("Declaration with id_code=" + idCode + " not found");
        }
        return result.get();
    }

    @Override
    public DeclarationDto findByNumber(String number) {
        Optional<Declaration> result = declarationRepository.findByNumber(number);
        if (result.isEmpty()){
            return DeclarationDto.builder().message(DECLARATION_NOT_FOUND_MESSAGE + number).build();
        }
        return declarationToDtoConverter.convert(result.get());
    }
}
