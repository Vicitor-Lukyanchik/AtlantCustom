package com.example.custom.service;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.entity.Declaration;

import java.util.List;

public interface DeclarationService {

    List<Declaration> importDeclarationsFromDbf();

    Declaration findByIdCode(Long idCode);

    DeclarationDto findByNumber(String number);
}
