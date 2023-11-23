package com.example.custom.test;

import com.example.custom.converter.DbfToDeclarationConverter;
import com.example.custom.dto.DbfDto;
import com.example.custom.entity.Declaration;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.reader.impl.DbfDosEncodingFileReaderImpl;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.impl.DeclarationServiceImpl;

import java.io.IOException;
import java.util.List;

public class MainTest {

    public static void main(String[] args) throws IOException {
//        System.out.println(g);

//        FIRMS = FIRMS.replaceAll("\\d","");
//        FIRMS = FIRMS.replaceAll("\\.","");
//        FIRMS = FIRMS.replaceAll(" ","");
        DbfToDeclarationConverter dbfToDeclarationConverter = new DbfToDeclarationConverter();
        DbfDosEncodingFileReader dbfDosEncodingFileReader = new DbfDosEncodingFileReaderImpl();
        List<Declaration> declarationsFromDbf = dbfToDeclarationConverter.convert(dbfDosEncodingFileReader.read("D:/GarbageExamples/BAZ_GTD.DBF"));
        System.out.println("Что-то там всякое");
    }

}
