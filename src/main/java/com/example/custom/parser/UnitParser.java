package com.example.custom.parser;

import com.example.custom.entity.Unit;
import com.example.custom.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UnitParser {

    private final UnitService unitService;

    public Unit parseUnit(List<Unit> units, Integer unitCode, String name) {
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
