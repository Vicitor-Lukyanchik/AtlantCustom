package com.example.custom.service;

import com.example.custom.dto.AllExportsDto;
import com.example.custom.dto.DeclarationDto;

public interface ImportService {

    void importImportsFromDbf(String path);

    AllExportsDto findImportByDeclarationDto(DeclarationDto declaration);

    void deleteAll();
}
