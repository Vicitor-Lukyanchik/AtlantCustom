package com.example.custom.service;

import com.example.custom.entity.Unit;

import java.util.List;

public interface UnitService {

    List<Unit> getAllUnits();

    Unit saveUnit(Unit unit);

    void deleteAll();
}
