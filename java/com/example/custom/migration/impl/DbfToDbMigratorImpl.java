package com.example.custom.migration.impl;

import com.example.custom.dto.MessageDto;
import com.example.custom.migration.DbfToDbMigrator;
import com.example.custom.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DbfToDbMigratorImpl implements DbfToDbMigrator {

    private static final String EMPTY_STRING = "";

    @Value(value = "${declaration.dbf}")
    private String declaration_dbf;

    @Value(value = "${export.dbf}")
    private String export_dbf;

    @Value(value = "${consumption.dbf}")
    private String consumption_dbf;

    @Value(value = "${arrival.dbf}")
    private String arrival_dbf;

    @Value(value = "${directory}")
    private String directory;

    private final ExportService exportService;
    private final ImportService importService;
    private final DeclarationService declarationService;
    private final CurrencyService currencyService;
    private final UnitService unitService;
    private final ChangelogService changelogService;

    @Override
    @Transactional
    public synchronized MessageDto migrateAll() {
        exportService.deleteAll();
        importService.deleteAll();
        declarationService.deleteAll();
        unitService.deleteAll();
        currencyService.deleteAll();
        changelogService.deleteAll();

        declarationService.importDeclarationsFromDbf(directory + declaration_dbf);
        exportService.importExportsFromDbf(directory + export_dbf);
        importService.importImportsFromDbf(directory + consumption_dbf, directory + arrival_dbf);
        return MessageDto.builder().message(EMPTY_STRING).build();
    }

    @Override
    @Transactional
    public synchronized MessageDto migrateLastUpdate() {
        try {
            declarationService.importLastUpdateDeclarationsFromDbf(directory + declaration_dbf);
            exportService.importLastUpdateExportsFromDbf(directory + export_dbf);
            importService.importLastUpdateImportsFromDbf(directory + consumption_dbf,
                    directory + arrival_dbf);
        } catch (Exception exception) {
            return MessageDto.builder().message(exception.getMessage()).build();
        }
        return MessageDto.builder().message(EMPTY_STRING).build();
    }

    @Override
    public synchronized Boolean isActive() {
        boolean isDeclarationActive = declarationService.isMigrateActive(directory + declaration_dbf);
        boolean isExportActive = exportService.isMigrateActive(directory + export_dbf);
        boolean isImportActive = importService.isMigrateActive(directory + consumption_dbf,
                directory + arrival_dbf);
        return isDeclarationActive && isExportActive && isImportActive;
    }

    @Override
    public synchronized void deleteAll() {
        exportService.deleteAll();
        importService.deleteAll();
        declarationService.deleteAll();
        unitService.deleteAll();
        currencyService.deleteAll();
        changelogService.deleteAll();
    }
}
