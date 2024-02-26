package com.example.custom.service;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.ImportDto;

public interface ImportService {

    void importImportsFromDbf(String consumptionPath, String arrivalPath);

    void importLastUpdateImportsFromDbf(String consumptionPath, String arrivalPath);

    Boolean isMigrateActive(String consumptionPath, String arrivalPath);

    ImportDto findImportByDeclarationDto(DeclarationDto declaration);

    void deleteAll();
}
