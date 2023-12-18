package com.example.custom.service;

import com.example.custom.dto.AllExportsDto;
import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.ImportByStorehouseDto;
import com.example.custom.dto.ImportDto;

public interface ImportService {

    void importImportsFromDbf(String path, String arrivalPath);

    ImportDto findImportByDeclarationDto(DeclarationDto declaration);
    ImportByStorehouseDto findImportByDeclarationDistributedByStorehouseDto(DeclarationDto declaration);

    void deleteAll();
}
