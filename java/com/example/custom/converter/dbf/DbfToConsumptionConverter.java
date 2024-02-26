package com.example.custom.converter.dbf;

import com.example.custom.converter.dto.DbfObjectToImportDbfDto;
import com.example.custom.dto.DbfDto;
import com.example.custom.dto.ImportDbfDto;
import com.example.custom.entity.Currency;
import com.example.custom.entity.Declaration;
import com.example.custom.entity.Consumption;
import com.example.custom.entity.Unit;
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
public class DbfToConsumptionConverter implements Converter<DbfDto, List<Consumption>> {

    private final UnitService unitService;
    private final CurrencyService currencyService;
    private final DbfObjectToImportDbfDto dbfToImportDbf;

    @Override
    public List<Consumption> convert(DbfDto dto) {
        List<Consumption> result = new ArrayList<>();
        List<Unit> units = unitService.getAllUnits();
        List<Currency> currencies = currencyService.getAllCurrencies();

        for (List<String> object : dto.getObjects()) {
            try {
                result.add(parseConsumption(object, units, currencies));
            } catch (Exception e) {
                System.out.println("Not find consumption with id_kod=" + object.get(0));
            }
        }
        return result;
    }

    private Consumption parseConsumption(List<String> object, List<Unit> units, List<Currency> currencies) {
        ImportDbfDto convert = dbfToImportDbf.convert(object, units, currencies);

        return Consumption.builder()
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
                .build();
    }

}
