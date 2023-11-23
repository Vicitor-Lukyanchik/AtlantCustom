package com.example.custom.service.impl;

import com.example.custom.converter.DbfToExportConverter;
import com.example.custom.converter.DtoToDeclarationConverter;
import com.example.custom.converter.ExportToDtoConverter;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.AllExportsDto;
import com.example.custom.dto.ExportDto;
import com.example.custom.entity.Export;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.ExportRepository;
import com.example.custom.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private static final String EMPTY_STRING = "";
    private static final String EXPORT_DBF_PATH = "D:/GarbageExamples/EXPORT.DBF";
    private static final String EXPORT_NOT_FOUND_BY_NUMBER_MESSAGE = "Экспорт не найден по декларации с номером ";

    private final ExportRepository exportRepository;
    private final DbfToExportConverter exportConverter;
    private final DbfDosEncodingFileReader dbfReader;
    private final DtoToDeclarationConverter dtoToDeclarationConverter;
    private final ExportToDtoConverter exportToDtoConverter;

    @Override
    public List<Export> importExportsFromDbf() {
        DbfDto dbfDto = dbfReader.read(EXPORT_DBF_PATH);
        List<Export> exports = exportConverter.convert(dbfDto);
        return saveExports(exports);
    }

    private List<Export> saveExports(List<Export> Export) {
        return exportRepository.saveAll(Export);
    }

    @Override
    public AllExportsDto findExportByDeclarationDto(DeclarationDto declaration) {
        List<Export> exports = findByDeclaration(declaration);
        if (exports.isEmpty()) {
            return AllExportsDto.builder().message(EXPORT_NOT_FOUND_BY_NUMBER_MESSAGE + declaration.getNumber()).build();
        }

        List<ExportDto> exportsDto = new ArrayList<>();
        for (Export export : exports) {
            exportsDto.add(exportToDtoConverter.convert(export));
        }

        return AllExportsDto.builder().exports(exportsDto).message(EMPTY_STRING).build();
    }

    private List<Export> findByDeclaration(DeclarationDto declaration) {
        return exportRepository.findAllByDeclaration(dtoToDeclarationConverter.convert(declaration));
    }
}
