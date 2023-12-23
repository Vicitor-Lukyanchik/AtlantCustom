package com.example.custom.migration.impl;

import com.example.custom.migration.DbfToDbMigrator;
import com.example.custom.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DbfToDbMigratorImpl implements DbfToDbMigrator {

    private static final String DECLARATION_DBF_PATH = "D:/GarbageExamples/BAZ_GTD.DBF";
    private static final String EXPORT_DBF_PATH = "D:/GarbageExamples/EXPORT.DBF";
    private static final String CONSUMPTION_DBF_PATH = "D:/GarbageExamples/RASH.DBF";
    private static final String ARRIVAL_DBF_PATH = "D:/GarbageExamples/PRIH.DBF";

    private final ExportService exportService;
    private final ImportService importService;
    private final DeclarationService declarationService;
    private final CurrencyService currencyService;
    private final UnitService unitService;

    @Override
    //@PostConstruct
    public void migrate() {
        //exportService.deleteAll();
        //importService.deleteAll();
        //declarationService.deleteAll();
        //unitService.deleteAll();
        //currencyService.deleteAll();

        //declarationService.importDeclarationsFromDbf(DECLARATION_DBF_PATH);
        //exportService.importExportsFromDbf(EXPORT_DBF_PATH);
        //importService.importImportsFromDbf(CONSUMPTION_DBF_PATH, ARRIVAL_DBF_PATH);
    }
}
