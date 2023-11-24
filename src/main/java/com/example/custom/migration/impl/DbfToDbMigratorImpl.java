package com.example.custom.migration.impl;

import com.example.custom.migration.DbfToDbMigrator;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.ExportService;
import com.example.custom.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbfToDbMigratorImpl implements DbfToDbMigrator {

    private static final String DECLARATION_DBF_PATH = "D:/GarbageExamples/BAZ_GTD.DBF";
    private static final String EXPORT_DBF_PATH = "D:/GarbageExamples/EXPORT.DBF";
    private static final String IMPORT_DBF_PATH = "D:/GarbageExamples/IMPORT.DBF";

    private final ExportService exportService;
    private final ImportService importService;
    private final DeclarationService declarationService;

    @Override
    @PostConstruct
    public void migrate() {
        exportService.deleteAll();
        //importService.deleteAll();
        declarationService.deleteAll();

        declarationService.importDeclarationsFromDbf(DECLARATION_DBF_PATH);
        exportService.importExportsFromDbf(EXPORT_DBF_PATH);
        //importService.importImportsFromDbf(IMPORT_DBF_PATH);
    }
}
