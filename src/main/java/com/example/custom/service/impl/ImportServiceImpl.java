package com.example.custom.service.impl;

import com.example.custom.converter.dbf.DbfToArrivalConverter;
import com.example.custom.converter.dbf.DbfToConsumptionConverter;
import com.example.custom.converter.dto.ArrivalToDtoConverter;
import com.example.custom.converter.dto.ConsumptionToDtoConverter;
import com.example.custom.converter.dto.DtoToDeclarationConverter;
import com.example.custom.converter.dto.ImportToByStorehouseConverter;
import com.example.custom.dto.*;
import com.example.custom.entity.Arrival;
import com.example.custom.entity.Consumption;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.ArrivalRepository;
import com.example.custom.repository.ConsumptionRepository;
import com.example.custom.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private static final String IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE = "Импорт не найден по декларации с номером ";
    private static final String EMPTY_STRING = "";

    private final DbfDosEncodingFileReader dbfReader;
    private final ConsumptionRepository consumptionRepository;
    private final ArrivalRepository arrivalRepository;
    private final DbfToConsumptionConverter dbfToConsumptionConverter;
    private final DbfToArrivalConverter dbfToArrivalConverter;
    private final DtoToDeclarationConverter dtoToDeclarationConverter;
    private final ConsumptionToDtoConverter consumptionToDtoConverter;
    private final ArrivalToDtoConverter arrivalToDtoConverter;
    private final ImportToByStorehouseConverter importToByStorehouseConverter;

    @Override
    public void importImportsFromDbf(String consumptionPath, String arrivalPath) {
        DbfDto consumptionDbfDto = dbfReader.read(consumptionPath);
        List<Consumption> consumptions = dbfToConsumptionConverter.convert(consumptionDbfDto);
        saveAllConsumptions(consumptions);

        DbfDto arrivalDbfDto = dbfReader.read(arrivalPath);
        List<Arrival> arrivals = dbfToArrivalConverter.convert(arrivalDbfDto);
        saveAllArrivals(arrivals);
    }

    @Override
    public ImportDto findImportByDeclarationDto(DeclarationDto declaration) {
        List<Consumption> consumptions = findConsumptionByDeclaration(declaration);
        List<Arrival> arrivals = findArrivalByDeclaration(declaration);
        if (consumptions.isEmpty() || arrivals.isEmpty()) {
            return ImportDto.builder().message(IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE + declaration.getNumber()).build();
        }

        return ImportDto.builder().arrivals(convertArrivalsToDto(arrivals))
                .consumptions(convertConsumptionsToDto(consumptions))
                .message(EMPTY_STRING).build();
    }

    @Override
    public ImportByStorehouseDto findImportByDeclarationDistributedByStorehouseDto(DeclarationDto declaration) {
        List<Consumption> consumptions = findConsumptionByDeclaration(declaration);
        List<Arrival> arrivals = findArrivalByDeclaration(declaration);
        if (consumptions.isEmpty() || arrivals.isEmpty()) {
            return ImportByStorehouseDto.builder().message(IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE + declaration.getNumber()).build();
        }

        return importToByStorehouseConverter.convert(ImportDto.builder().arrivals(convertArrivalsToDto(arrivals))
                .consumptions(convertConsumptionsToDto(consumptions))
                .message(EMPTY_STRING).build());
    }

    private List<ConsumptionDto> convertConsumptionsToDto(List<Consumption> consumptions) {
        List<ConsumptionDto> result = new ArrayList<>();
        for (Consumption consumption : consumptions) {
            result.add(consumptionToDtoConverter.convert(consumption));
        }
        return result;
    }

    private List<ArrivalDto> convertArrivalsToDto(List<Arrival> arrivals) {
        List<ArrivalDto> result = new ArrayList<>();
        for (Arrival arrival : arrivals) {
            result.add(arrivalToDtoConverter.convert(arrival));
        }
        return result;
    }

    private void saveAllArrivals(List<Arrival> arrivals) {
        arrivalRepository.saveAll(arrivals);
    }

    private void saveAllConsumptions(List<Consumption> consumptions) {
        consumptionRepository.saveAll(consumptions);
    }

    private List<Consumption> findConsumptionByDeclaration(DeclarationDto declaration) {
        return consumptionRepository.findAllByDeclaration(dtoToDeclarationConverter.convert(declaration));
    }

    private List<Arrival> findArrivalByDeclaration(DeclarationDto declaration) {
        return arrivalRepository.findAllByDeclaration(dtoToDeclarationConverter.convert(declaration));
    }

    @Override
    @Transactional
    public void deleteAll() {
        consumptionRepository.deleteAll();
        arrivalRepository.deleteAll();
    }
}
