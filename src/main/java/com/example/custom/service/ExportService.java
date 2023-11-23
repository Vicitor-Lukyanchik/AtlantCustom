package com.example.custom.service;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.AllExportsDto;
import com.example.custom.entity.Export;

import java.util.List;

public interface ExportService {

    List<Export> importExportsFromDbf();

    AllExportsDto findExportByDeclarationDto(DeclarationDto declaration);
}
