package com.example.custom.converter;

import com.example.custom.dto.DbfDto;
import com.example.custom.entity.Declaration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.custom.converter.Parser.*;

@Component
public class DbfToDeclarationConverter implements Converter<DbfDto, List<Declaration>> {

    @Override
    public List<Declaration> convert(DbfDto dto) {
        List<Declaration> result = new ArrayList<>();
        NumberFormat numberFormat = NumberFormat.getInstance();

        for (List<String> object : dto.getObjects()) {
            Long idKod = parseLong(object.get(0));
            Boolean vid = isImport(parseInteger(object.get(1)));
            Integer rej = parseInteger(object.get(2));
            String namRej = object.get(3);
            LocalDate datGtd = parseToDate(object.get(4));
            String gtd = object.get(5);
            String gtdB = object.get(6);
            String nDog = object.get(7);
            LocalDate dDoc = parseToDate(object.get(8));
            String nFirm = object.get(9);

            result.add(Declaration.builder()
                    .idCode(idKod)
                    .isImport(vid)
                    .date(datGtd)
                    .number(gtd)
                    .importApplicationNumber(gtdB)
                    .contractNumber(nDog)
                    .contractDate(dDoc)
                    .firmName(nFirm)
                    .build());
        }

        return result;
    }
}
