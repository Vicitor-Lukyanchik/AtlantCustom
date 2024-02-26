package com.example.custom.service.impl;

import com.example.custom.converter.dbf.DbfToDeclarationConverter;
import com.example.custom.converter.dto.DeclarationToDtoConverter;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Changelog;
import com.example.custom.entity.Declaration;
import com.example.custom.exception.ServiceException;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.DeclarationRepository;
import com.example.custom.service.ChangelogService;
import com.example.custom.service.DeclarationService;
import com.example.custom.util.CheckerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeclarationServiceImpl implements DeclarationService {

    private static final String DECLARATION_NOT_FOUND_MESSAGE = "Не найдена декларация с номером ";
    private static final String NUMBER_IS_EMPTY_MESSAGE = "Номер таможенного документа не может быть пустым";
    private static final String CHANGELOG_NAME = "Declaration";

    private final DbfToDeclarationConverter dbfToDeclarationConverter;
    private final DbfDosEncodingFileReader dbfReader;
    private final DeclarationRepository declarationRepository;
    private final DeclarationToDtoConverter declarationToDtoConverter;
    private final ChangelogService changelogService;
    private final CheckerUtil checkerUtil;

    private DbfDto declarationDbfDto;

    @Override
    @Transactional
    public void importDeclarationsFromDbf(String path) {
        DbfDto dbfDto = read(path);
        List<Declaration> declarations = dbfToDeclarationConverter.convert(dbfDto);
        changelogService.saveChangelog(Changelog.builder()
                .filePath(path).name(CHANGELOG_NAME).addedCount(dbfDto.getObjects().size())
                .allCount(dbfDto.getObjects().size()).localDateTime(LocalDateTime.now())
                .build());
        saveDeclarations(declarations);
    }

    @Override
    @Transactional
    public void importLastUpdateDeclarationsFromDbf(String path) {
        DbfDto dbfDto = read(path);
        Changelog changelog = changelogService.getOrSaveChangelogByName(CHANGELOG_NAME);

        if (dbfDto.getObjects().size() != changelog.getAllCount()) {


            List<Declaration> declarations = dbfToDeclarationConverter.convert(DbfDto.builder()
                    .columns(dbfDto.getColumns())
                    .objects(dbfDto.getObjects().subList(changelog.getAllCount(), dbfDto.getObjects().size()))
                    .build());

            changelogService.saveChangelog(Changelog.builder()
                    .filePath(path).name(CHANGELOG_NAME).addedCount(dbfDto.getObjects().size() - changelog.getAllCount())
                    .allCount(dbfDto.getObjects().size()).localDateTime(LocalDateTime.now())
                    .build());
            saveDeclarations(declarations);
        }
    }

    @Override
    public Boolean isMigrateActive(String path) {
        deleteReaderCache();
        DbfDto dbfDto = dbfReader.read(path);
        Changelog changelog = changelogService.getOrSaveChangelogByName(CHANGELOG_NAME);
        if (changelog.getAllCount() == null) {
            return false;
        }
        return changelog.getAllCount() == dbfDto.getObjects().size();
    }

    private DbfDto read(String path) {
        if (this.declarationDbfDto == null) {
            this.declarationDbfDto = dbfReader.read(path);
        }
        return this.declarationDbfDto.toBuilder().build();
    }

    private void deleteReaderCache() {
        this.declarationDbfDto = null;
    }

    @Transactional
    private List<Declaration> saveDeclarations(List<Declaration> declarations) {
        return declarationRepository.saveAll(declarations);
    }

    @Override
    public Declaration findByIdCode(Long idCode) {
        Optional<Declaration> result = declarationRepository.findByIdCode(idCode);
        if (result.isEmpty()) {
            throw new ServiceException("Declaration with id_code=" + idCode + " not found");
        }
        return result.get();
    }

    @Override
    public DeclarationDto findByNumber(String number) {
        String message = checkerUtil.checkDeclarationNumber(number);
        if (!message.isEmpty()) {
            return DeclarationDto.builder().message(message).build();
        }

        Optional<Declaration> result = declarationRepository.findByNumber(number);

        if (number.isEmpty()) {
            return DeclarationDto.builder().message(NUMBER_IS_EMPTY_MESSAGE + number).build();
        } else if (result.isEmpty()) {
            return DeclarationDto.builder().message(DECLARATION_NOT_FOUND_MESSAGE + number).build();
        }
        return declarationToDtoConverter.convert(result.get());
    }

    @Override
    @Transactional
    public void deleteAll() {
        declarationRepository.deleteAll();
        declarationRepository.setAutoincrementOnStart();
    }
}
