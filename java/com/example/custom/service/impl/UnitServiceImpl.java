package com.example.custom.service.impl;

import com.example.custom.entity.Unit;
import com.example.custom.repository.UnitRepository;
import com.example.custom.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    @Override
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @Override
    public Unit saveUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    @Override
    public void deleteAll() {
        unitRepository.deleteAll();
    }
}
