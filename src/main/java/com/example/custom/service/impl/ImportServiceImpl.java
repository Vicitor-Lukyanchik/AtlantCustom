package com.example.custom.service.impl;

import com.example.custom.converter.DbfToImportConverter;
import com.example.custom.converter.DtoToDeclarationConverter;
import com.example.custom.dto.AllExportsDto;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Import;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.ImportRepository;
import com.example.custom.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private static final String IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE = "Импорт не найден по декларации с номером ";

    private final DbfDosEncodingFileReader dbfReader;
    private final ImportRepository importRepository;
    private final DbfToImportConverter dbfToImportConverter;
    private final DtoToDeclarationConverter dtoToDeclarationConverter;

    @Override
    public void importImportsFromDbf(String path) {
        DbfDto dbfDto = dbfReader.read(path);
        List<Import> imports = dbfToImportConverter.convert(dbfDto);
    }

    @Override
    public AllExportsDto findImportByDeclarationDto(DeclarationDto declaration) {
        List<Import> imports = findByDeclaration(declaration);
        if (imports.isEmpty()) {
            return AllExportsDto.builder().message(IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE + declaration.getNumber()).build();
        }

        return null;
    }

    private List<Import> findByDeclaration(DeclarationDto declaration) {
        return importRepository.findAllByDeclaration(dtoToDeclarationConverter.convert(declaration));
    }
    @Override
    public void deleteAll() {
        importRepository.deleteAll();
    }
}
