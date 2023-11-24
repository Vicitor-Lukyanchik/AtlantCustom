package com.example.custom.converter;

import com.example.custom.dto.DbfDto;
import com.example.custom.entity.Declaration;
import com.example.custom.entity.Import;
import com.example.custom.entity.Unit;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.custom.parser.DataTypeParser.*;

@Component
@RequiredArgsConstructor
public class DbfToImportConverter implements Converter<DbfDto, List<Import>> {

    private final DeclarationService declarationService;
    private final UnitService unitService;

    @Override
    public List<Import> convert(DbfDto dto) {
        List<Import> result = new ArrayList<>();
        List<Unit> units = unitService.getAllUnits();

        for (List<String> object : dto.getObjects()) {
            if (!object.get(0).equals("null")) {
                try {
                    result.add(parseImport(object, units));
                } catch (Exception e) {
                    System.out.println("Not find declaration with id_kod=" + object.get(0));
                }
            }
        }
        return result;
    }

    private Import parseImport(List<String> object, List<Unit> units) {
        Declaration declaration = declarationService.findByIdCode(parseLong(object.get(0)));
        object.get(1);
        object.get(2);
        String tnved = object.get(3);



        return Import.builder()
                .declaration(declaration)
                .tnved(tnved)
                .build();
    }

}
