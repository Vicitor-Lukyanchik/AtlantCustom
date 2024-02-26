package com.example.custom.service;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Declaration;

import java.util.List;

public interface DeclarationService {

    void importDeclarationsFromDbf(String path);

    void importLastUpdateDeclarationsFromDbf(String path);

    Boolean isMigrateActive(String path);

    Declaration findByIdCode(Long idCode);

    DeclarationDto findByNumber(String number);

    void deleteAll();
}
