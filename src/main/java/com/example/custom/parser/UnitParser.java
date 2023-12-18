package com.example.custom.parser;

import com.example.custom.entity.Unit;

import java.util.List;

public interface UnitParser {

    Unit parseUnit(List<Unit> units, Integer unitCode, String name);

    Unit parseUnit(List<Unit> units, String name);
}
