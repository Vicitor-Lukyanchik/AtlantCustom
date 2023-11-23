package com.example.custom.converter;

import com.example.custom.dto.DbfDto;
import com.example.custom.entity.Declaration;
import com.example.custom.entity.Export;
import com.example.custom.entity.Unit;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.custom.converter.Parser.*;

@Component
@RequiredArgsConstructor
public class DbfToExportConverter implements Converter<DbfDto, List<Export>> {

    private final DeclarationService declarationService;
    private final UnitService unitService;

    @Override
    public List<Export> convert(DbfDto dto) {
        List<Export> result = new ArrayList<>();
        List<Unit> units = unitService.getAllUnits();

        for (List<String> object : dto.getObjects()) {
            if (!object.get(0).equals("null")) {
                try {
                    result.add(parseExport(object, units));
                } catch (Exception e) {
                    System.out.println("Not find declaration with id_kod=" + object.get(0));
                }
            }
        }
        return result;
    }

    private Export parseExport(List<String> object, List<Unit> units) {
        Declaration declaration = declarationService.findByIdCode(parseLong(object.get(0)));
        object.get(1);
        object.get(2);
        String tnved = object.get(3);
        String productName = object.get(4).replace("\"", "'");
        String ttnSeries = object.get(5);
        String ttnNumber = object.get(6);
        LocalDate ttnDate = parseToDate(object.get(7));
        object.get(8);
        object.get(9);
        object.get(10);
        Integer firmCode = parseInteger(object.get(11));
        String firmName = object.get(12);
        Double productWeight = parseDouble(object.get(13));
        Integer productCount = parseInteger(object.get(14));
        Unit unit = parseUnit(units, parseInteger(object.get(15)), object.get(16));


        BigDecimal productSum = new BigDecimal(object.get(17));
        LocalDateTime dateTime = parseLocalDateTime(object.get(18), object.get(19));
        String fio = object.get(20);

        return Export.builder()
                .declaration(declaration)
                .tnved(tnved)
                .productName(productName)
                .ttnSeries(ttnSeries)
                .ttnNumber(ttnNumber)
                .ttnDate(ttnDate)
                .productCount(productCount)
                .productWeight(productWeight)
                .productSum(productSum)
                .unit(unit)
                .dateTime(dateTime)
                .fio(fio)
                .build();
    }

    private Unit parseUnit(List<Unit> units, Integer unitCode, String name) {
        for (Unit unit : units) {
            if (Objects.equals(unit.getUnitCode(), unitCode)) {
                return unit;
            }
        }
        Unit unit = unitService.saveUnit(Unit.builder()
                .unitCode(unitCode)
                .unitName(name).build());
        units.add(unit);
        return unit;
    }
}
