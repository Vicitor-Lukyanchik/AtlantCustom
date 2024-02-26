package com.example.custom.converter.dto;

import com.example.custom.dto.ConsumptionDto;
import com.example.custom.dto.ImportDbfDto;
import com.example.custom.entity.Consumption;
import com.example.custom.entity.Currency;
import com.example.custom.entity.Unit;
import com.example.custom.parser.CurrencyParser;
import com.example.custom.parser.UnitParser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.custom.parser.DataTypeParser.*;

@Component
@RequiredArgsConstructor
public class DbfObjectToImportDbfDto {

    private final UnitParser unitParser;
    private final CurrencyParser currencyParser;

    public ImportDbfDto convert(List<String> object, List<Unit> units, List<Currency> currencies) {

        Long id = getLongObject(object.get(1));
        String nom_sklad = object.get(2);
        LocalDate datao = parseToDate(object.get(3));
        Integer typ = parseInteger(object.get(4));
        String nomno = object.get(5);
        String name = object.get(6);
        Double kolobor = parseDouble(object.get(7));
        Unit unit = unitParser.parseUnit(units, object.get(8));
        BigDecimal cenao = new BigDecimal(object.get(9)).setScale(2);

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

        return ImportDbfDto.builder()
                .id(id)
                .nom_sklad(nom_sklad)
                .datao(datao)
                .typ(typ)
                .nomno(nomno)
                .name(name)
                .kolobor(kolobor)
                .unit(unit)
                .cenao(cenao)
                .currency(currency)
                .shpolo(shpolo)
                .nam_sh(nam_sh)
                .doco(doco)
                .nam_meso(nam_meso)
                .god(god)
                .doc_tr(doc_tr)
                .ser_tr(ser_tr)
                .dat_tr(dat_tr)
                .nom_f(nom_f)
                .dat_f(dat_f)
                .d_nd(d_nd)
                .d_dath(d_dath)
                .d_kd(d_kd)
                .pol_ceh(pol_ceh)
                .d_tam_doc(d_tam_doc)
                .build();
    }
}
