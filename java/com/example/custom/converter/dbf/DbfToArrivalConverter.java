package com.example.custom.converter.dbf;

import com.example.custom.converter.dto.DbfObjectToImportDbfDto;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.ImportDbfDto;
import com.example.custom.entity.*;
import com.example.custom.parser.CurrencyParser;
import com.example.custom.parser.UnitParser;
import com.example.custom.service.CurrencyService;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.custom.parser.DataTypeParser.*;

@Component
@RequiredArgsConstructor
public class DbfToArrivalConverter implements Converter<DbfDto, List<Arrival>> {

    private final DeclarationService declarationService;
    private final UnitService unitService;
    private final CurrencyService currencyService;
    private final DbfObjectToImportDbfDto dbfToImportDbf;

    @Override
    public List<Arrival> convert(DbfDto dto) {
        List<Arrival> result = new ArrayList<>();
        List<Unit> units = unitService.getAllUnits();
        List<Currency> currencies = currencyService.getAllCurrencies();

        for (List<String> object : dto.getObjects()) {
            if (!object.get(0).equals("null") && !object.get(0).equals("0.0")) {
                try {
                    result.add(parseArrival(object, units, currencies));
                } catch (Exception e) {
                    System.out.println("Not find arrival with id_kod=" + object.get(0));
                }
            }
        }
        return result;
    }

    private Arrival parseArrival(List<String> object, List<Unit> units, List<Currency> currencies) {
        Long idCode = parseLong(object.get(0));
        Declaration declaration = declarationService.findByIdCode(idCode);
        ImportDbfDto convert = dbfToImportDbf.convert(object, units, currencies);

        return Arrival.builder()
                .declaration(declaration)
                .storehouseId(convert.getId())
                .storehouseNumber(convert.getNom_sklad())
                .arrivalDate(convert.getDatao())
                .documentType(convert.getTyp())
                .productCode(convert.getNomno())
                .productName(convert.getName())
                .turnoverCount(convert.getKolobor())
                .unit(convert.getUnit())
                .productPrice(convert.getCenao())
                .currency(convert.getCurrency())
                .externalReceiverCode(convert.getShpolo())
                .externalReceiverName(convert.getNam_sh())
                .documentNumber(convert.getDoco())
                .reportingMonth(convert.getNam_meso())
                .reportingYear(convert.getGod())
                .accompanyingDocumentNumber(convert.getDoc_tr())
                .accompanyingDocumentSeries(convert.getSer_tr())
                .accompanyingDocumentDate(convert.getDat_tr())
                .workshopReceiver(convert.getPol_ceh())
                .applicationNumber(convert.getD_tam_doc())
                .consumptions(new ArrayList<>())
                .build();
    }
}
