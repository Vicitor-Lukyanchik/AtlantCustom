package com.example.custom.converter.dbf;

import com.example.custom.dto.DbfDto;
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

    private final DeclarationService declarationService;
    private final UnitService unitService;
    private final CurrencyService currencyService;
    private final UnitParser unitParser;
    private final CurrencyParser currencyParser;

    @Override
    public List<Consumption> convert(DbfDto dto) {
        List<Consumption> result = new ArrayList<>();
        List<Unit> units = unitService.getAllUnits();
        List<Currency> currencies = currencyService.getAllCurrencies();

         for (List<String> object : dto.getObjects()) {
            //if (!object.get(0).equals("null")) {
                try {
                    result.add(parseConsumption(object, units, currencies));
                } catch (Exception e) {
                    System.out.println("Not find consumption with id_kod=" + object.get(0));
                }
            //}
        }
        return result;
    }

    private Consumption parseConsumption(List<String> object, List<Unit> units, List<Currency> currencies) {
        //Declaration declaration = declarationService.findByIdCode(parseLong(object.get(0)));
        Long id = parseLong(object.get(1));
        String nom_sklad = object.get(2);
        LocalDate datao = parseToDate(object.get(3));
        Integer typ = parseInteger(object.get(4));
        String nomno = object.get(5);
        String name = object.get(6);
        Double kolobor = parseDouble(object.get(7));
        Unit unit = unitParser.parseUnit(units, object.get(8));
        BigDecimal cenao = new BigDecimal(object.get(9));

        Integer kod_v = parseInteger(object.get(10));
        String nam_v = object.get(11);
        Currency currency = currencyParser.parseCurrency(currencies, kod_v, nam_v);

        Integer shpolo = parseInteger(object.get(12));
        String nam_sh = object.get(13);
        Integer doco = parseInteger(object.get(14));
        Integer nam_meso = parseInteger(object.get(15));
        Integer god = parseInteger(object.get(16));
        String doc_tr = object.get(17);
        String ser_tr = object.get(18);
        LocalDate dat_tr = parseToDate(object.get(19));
        String nom_f = object.get(20);
        LocalDate dat_f = parseToDate(object.get(21));
        String d_nd = object.get(22);
        LocalDate d_dath = parseToDate(object.get(23));
        Integer d_kd = parseInteger(object.get(24));
        String pol_ceh = object.get(25);
        String d_tam_doc = object.get(26);


        return Consumption.builder()
                .declaration(null)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                .storehouseId(id)
                .storehouseNumber(nom_sklad)
                .arrivalDate(datao)
                .documentType(typ)
                .productCode(nomno)
                .productName(name)
                .turnoverCount(kolobor)
                .unit(unit)
                .productPrice(cenao)
                .currency(currency)

                .externalReceiverCode(shpolo)
                .externalReceiverName(nam_sh)
                .documentNumber(doco)
                .reportingMonth(nam_meso)
                .reportingYear(god)
                .accompanyingDocumentNumber(doc_tr)
                .accompanyingDocumentSeries(ser_tr)
                .accompanyingDocumentDate(dat_tr)


                .workshopReceiver(pol_ceh)
                .applicationNumber(d_tam_doc)
                .build();
    }

}
