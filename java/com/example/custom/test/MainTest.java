package com.example.custom.test;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Currency;
import com.example.custom.entity.Declaration;
import com.example.custom.entity.Unit;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;

public class MainTest {

    private static final DateFormat dateFormat = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    public static void main(String[] args) throws IOException, ParseException {
        Declaration declaration = new Declaration();
        Unit unit =new Unit();
        Currency currency = new Currency();

        Arrival arrival = Arrival.builder()
                .declaration(declaration)
                .storehouseId(1l)
                .storehouseNumber("73")
                .productCode("657765")
                .productName("Kompressor")
                .turnoverCount(2.)
                .productPrice(new BigDecimal(23))
                .arrivalDate(dateFormat.parse("2022 04 11").toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .unit(unit)
                .currency(currency)

                .externalReceiverCode(1)
                .externalReceiverName("Mzh")

                .documentType(0)
                .documentNumber(123212)
                .reportingMonth(4)
                .reportingYear(2022)

                .accompanyingDocumentSeries("AA")
                .accompanyingDocumentNumber("839483")
                .accompanyingDocumentDate(dateFormat.parse("2022 04 11").toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())

                .nom_f("nom_f")
                .dat_f(dateFormat.parse("2022 05 11").toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())

                .workshopReceiver("Starolliteyni")
                .applicationNumber("n02h67")
                .build();
    }
}
