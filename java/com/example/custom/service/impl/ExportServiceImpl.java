package com.example.custom.service.impl;

import com.example.custom.converter.dbf.DbfToExportConverter;
import com.example.custom.converter.dto.DtoToDeclarationConverter;
import com.example.custom.converter.dto.ExportToDtoConverter;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.AllExportsDto;
import com.example.custom.dto.ExportDto;
import com.example.custom.entity.Changelog;
import com.example.custom.entity.Export;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.ExportRepository;
import com.example.custom.service.ChangelogService;
import com.example.custom.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private static final String EMPTY_STRING = "";
    private static final String EXPORT_NOT_FOUND_BY_NUMBER_MESSAGE = "Экспорт не найден по декларации с номером ";
    private static final String CHANGELOG_NAME = "Export";


    private final ExportRepository exportRepository;
    private final DbfToExportConverter exportConverter;
    private final DbfDosEncodingFileReader dbfReader;
    private final DtoToDeclarationConverter dtoToDeclarationConverter;
    private final ExportToDtoConverter exportToDtoConverter;
    private final ChangelogService changelogService;

    private DbfDto exportDbfDto;

    @Override
    @Transactional
    public void importExportsFromDbf(String path) {
        DbfDto dbfDto = read(path);
        List<Export> exports = exportConverter.convert(dbfDto);
        changelogService.saveChangelog(Changelog.builder()
                .filePath(path).name(CHANGELOG_NAME).addedCount(dbfDto.getObjects().size())
                .allCount(dbfDto.getObjects().size()).localDateTime(LocalDateTime.now())
                .build());
        saveExports(exports);
    }

    @Override
    @Transactional
    public void importLastUpdateExportsFromDbf(String path) {
        DbfDto dbfDto = read(path);
        Changelog changelog = changelogService.getOrSaveChangelogByName(CHANGELOG_NAME);

        if (dbfDto.getObjects().size() != changelog.getAllCount()) {
            List<Export> exports = exportConverter.convert(DbfDto.builder()
                    .columns(dbfDto.getColumns())
                    .objects(dbfDto.getObjects().subList(changelog.getAllCount(), dbfDto.getObjects().size()))
                    .build());

            changelogService.saveChangelog(Changelog.builder()
                    .filePath(path).name(CHANGELOG_NAME).addedCount(dbfDto.getObjects().size()-changelog.getAllCount())
                    .allCount(dbfDto.getObjects().size()).localDateTime(LocalDateTime.now())
                    .build());
            saveExports(exports);
        }
    }

    @Override
    public Boolean isMigrateActive(String path) {
        deleteReaderCache();
        DbfDto dbfDto = read(path);
        Changelog changelog = changelogService.getOrSaveChangelogByName(CHANGELOG_NAME);
        if (changelog.getAllCount() == null) {
            return false;
        }
        return changelog.getAllCount()==dbfDto.getObjects().size();
    }

    private DbfDto read(String path) {
        if (this.exportDbfDto == null) {
            this.exportDbfDto = dbfReader.read(path);
        }
        return this.exportDbfDto.toBuilder().build();
    }

    private void deleteReaderCache() {
        this.exportDbfDto = null;
    }

    @Transactional
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

    @Override
    @Transactional
    public void deleteAll() {
        exportRepository.deleteAll();
        exportRepository.setAutoincrementOnStart();
    }
}
