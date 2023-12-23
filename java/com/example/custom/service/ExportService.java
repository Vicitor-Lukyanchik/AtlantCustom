package com.example.custom.service;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.AllExportsDto;

public interface ExportService {

    void importExportsFromDbf(String path);

    AllExportsDto findExportByDeclarationDto(DeclarationDto declaration);

    void deleteAll();
}
