package com.example.custom.service.impl;

import com.example.custom.converter.dbf.DbfToArrivalConverter;
import com.example.custom.converter.dbf.DbfToConsumptionConverter;
import com.example.custom.converter.dto.ArrivalToDtoConverter;
import com.example.custom.converter.dto.ConsumptionToDtoConverter;
import com.example.custom.converter.dto.DtoToDeclarationConverter;
import com.example.custom.dto.*;
import com.example.custom.entity.Arrival;
import com.example.custom.entity.Changelog;
import com.example.custom.entity.Consumption;
import com.example.custom.exception.ServiceException;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.example.custom.repository.ArrivalRepository;
import com.example.custom.repository.ConsumptionRepository;
import com.example.custom.service.ChangelogService;
import com.example.custom.service.ImportService;
import com.example.custom.tier.ConsumptionTier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private static final String IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE = "Импорт не найден по декларации с номером ";
    private static final String EMPTY_STRING = "";
    private static final String CONSUMPTION_CHANGELOG_NAME = "Consumption";
    private static final String ARRIVAL_CHANGELOG_NAME = "Arrival";
    private static final String NON_ARRIVAL_NAME = "NonArrivalsConsumptions";
    private static final int START_PAST = 0;


    private final DbfDosEncodingFileReader dbfReader;
    private final ConsumptionRepository consumptionRepository;
    private final ArrivalRepository arrivalRepository;
    private final DbfToConsumptionConverter dbfToConsumptionConverter;
    private final DbfToArrivalConverter dbfToArrivalConverter;
    private final DtoToDeclarationConverter dtoToDeclarationConverter;
    private final ConsumptionToDtoConverter consumptionToDtoConverter;
    private final ArrivalToDtoConverter arrivalToDtoConverter;
    private final ConsumptionTier consumptionTier;
    private final ChangelogService changelogService;

    private DbfDto consumptionDbfDto;
    private DbfDto arrivalDbfDto;


    @Override
    @Transactional
    public void importImportsFromDbf(String consumptionPath, String arrivalPath) {
        DbfDto consumptionDbfDto = readConsumption(consumptionPath);
        DbfDto arrivalDbfDto = readArrival(arrivalPath);
        List<Consumption> consumptions = dbfToConsumptionConverter.convert(consumptionDbfDto);
        List<Arrival> arrivals = dbfToArrivalConverter.convert(arrivalDbfDto);

        tieArrivalsToConsumption(arrivals, consumptions);

        saveChangelogs(consumptionPath, arrivalPath, consumptionDbfDto.getObjects().size(), START_PAST, arrivalDbfDto.getObjects().size(), START_PAST);
    }

    @Override
    @Transactional
    public void importLastUpdateImportsFromDbf(String consumptionPath, String arrivalPath) {
        Changelog consumptionChangelog = changelogService.getOrSaveChangelogByName(CONSUMPTION_CHANGELOG_NAME);
        Changelog arrivalChangelog = changelogService.getOrSaveChangelogByName(ARRIVAL_CHANGELOG_NAME);
        DbfDto consumptionDbfDto = readConsumption(consumptionPath);
        DbfDto arrivalDbfDto = readArrival(arrivalPath);

        if (consumptionChangelog.getAllCount() != consumptionDbfDto.getObjects().size() ||
                arrivalChangelog.getAllCount() != arrivalDbfDto.getObjects().size()) {
            List<Consumption> consumptions = dbfToConsumptionConverter.convert(DbfDto.builder()
                    .columns(consumptionDbfDto.getColumns())
                    .objects(consumptionDbfDto.getObjects().subList(consumptionChangelog.getAllCount(), consumptionDbfDto.getObjects().size())).build());
            consumptions.addAll(getNonArrivalsConsumptions());

            List<Arrival> arrivals = dbfToArrivalConverter.convert(DbfDto.builder()
                    .columns(arrivalDbfDto.getColumns())
                    .objects(arrivalDbfDto.getObjects().subList(arrivalChangelog.getAllCount(), arrivalDbfDto.getObjects().size())).build());
            arrivals.addAll(arrivalRepository.findNotReadyArrivals());

            tieArrivalsToConsumption(arrivals, consumptions);

            saveChangelogs(consumptionPath, arrivalPath, consumptionDbfDto.getObjects().size(),
                    consumptionChangelog.getAllCount(), arrivalDbfDto.getObjects().size(), arrivalChangelog.getAllCount());
        }
    }

    @Override
    public Boolean isMigrateActive(String consumptionPath, String arrivalPath) {
        deleteReaderCache();
        DbfDto consumptionDbfDto = readConsumption(consumptionPath);
        DbfDto arrivalDbfDto = readArrival(arrivalPath);
        Changelog consumptionChangelog = changelogService.getOrSaveChangelogByName(CONSUMPTION_CHANGELOG_NAME);
        Changelog arrivalChangelog = changelogService.getOrSaveChangelogByName(ARRIVAL_CHANGELOG_NAME);
        if (arrivalChangelog.getAllCount() == null || consumptionChangelog.getAllCount() == null) {
            return false;
        }
        return arrivalChangelog.getAllCount() == arrivalDbfDto.getObjects().size() &&
                consumptionChangelog.getAllCount() == consumptionDbfDto.getObjects().size();
    }

    private DbfDto readConsumption(String consumptionPath) {
        if (this.consumptionDbfDto == null) {
            this.consumptionDbfDto = dbfReader.read(consumptionPath);
        }
        return this.consumptionDbfDto.toBuilder().build();
    }

    private DbfDto readArrival(String arrivalPath) {
        if (this.arrivalDbfDto == null) {
            this.arrivalDbfDto = dbfReader.read(arrivalPath);
        }
        return this.arrivalDbfDto.toBuilder().build();
    }

    private void deleteReaderCache() {
        this.consumptionDbfDto = null;
        this.arrivalDbfDto = null;
    }

    private void tieArrivalsToConsumption(List<Arrival> arrivals, List<Consumption> consumptions) {
        Arrival nonArrivalConsumptions = arrivalRepository.findArrivalByProductName(NON_ARRIVAL_NAME);
        if (nonArrivalConsumptions == null) {
            nonArrivalConsumptions = arrivalRepository.save(Arrival.builder().productName(NON_ARRIVAL_NAME).consumptions(new ArrayList<>()).turnoverCount(.0).build());
        }
        try {
            List<Arrival> tie = new ArrayList<>(consumptionTier.tie(arrivals, consumptions, nonArrivalConsumptions));
            saveAllArrivals(tie);
        } catch (Exception exception) {
            throw new ServiceException(exception.getMessage());
        }
    }

    private void saveChangelogs(String consumptionPath, String arrivalPath, int consumptionAllCount, int consumptionPastCount, int arrivalAllCount, int arrivalPastCount) {
        changelogService.saveChangelog(Changelog.builder()
                .filePath(consumptionPath).name(CONSUMPTION_CHANGELOG_NAME).addedCount(consumptionAllCount - consumptionPastCount)
                .allCount(consumptionAllCount).localDateTime(LocalDateTime.now())
                .build());
        changelogService.saveChangelog(Changelog.builder()
                .filePath(arrivalPath).name(ARRIVAL_CHANGELOG_NAME).addedCount(arrivalAllCount - arrivalPastCount)
                .allCount(arrivalAllCount).localDateTime(LocalDateTime.now())
                .build());
    }

    @Override
    public ImportDto findImportByDeclarationDto(DeclarationDto declaration) {
        List<Arrival> arrivals = findArrivalByDeclaration(declaration);
        List<Consumption> consumptions = getAllConsumptions(arrivals);
        if (arrivals.isEmpty()) {
            return ImportDto.builder().message(IMPORT_NOT_FOUND_BY_NUMBER_MESSAGE + declaration.getNumber()).build();
        }

        return ImportDto.builder().arrivals(convertArrivalsToDto(arrivals))
                .consumptions(convertConsumptionsToDto(consumptions))
                .message(EMPTY_STRING).build();
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

    private List<Consumption> getNonArrivalsConsumptions() {
        Arrival notReadyArrival = arrivalRepository.findArrivalByProductName(NON_ARRIVAL_NAME);
        if (notReadyArrival == null) {
            return new ArrayList<>();
        }
        return notReadyArrival.getConsumptions();
    }

    @Transactional
    private void saveAllArrivals(List<Arrival> arrivals) {
        for (Arrival arrival : arrivals) {
            List<Consumption> consumptions = new ArrayList<>(arrival.getConsumptions());
            arrivalRepository.save(arrival);
            consumptionRepository.saveAll(consumptions);
        }
    }

    public List<Consumption> getAllConsumptions(List<Arrival> arrivals) {
        List<Consumption> consumptions = new ArrayList<>();
        for (Arrival arrival : arrivals) {
            consumptions.addAll(arrival.getConsumptions());
        }
        return consumptions;
    }

    private List<Arrival> findArrivalByDeclaration(DeclarationDto declaration) {
        return arrivalRepository.findAllByDeclaration(dtoToDeclarationConverter.convert(declaration));
    }

    @Override
    @Transactional
    public void deleteAll() {
        consumptionRepository.deleteAll();
        consumptionRepository.setAutoincrementOnStart();
        arrivalRepository.deleteAll();
        arrivalRepository.setAutoincrementOnStart();
    }
}
