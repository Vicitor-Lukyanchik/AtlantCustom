package com.example.custom.parser.impl;

import com.example.custom.entity.Currency;
import com.example.custom.entity.Unit;
import com.example.custom.parser.CurrencyParser;
import com.example.custom.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CurrencyParserImpl implements CurrencyParser {

    private final CurrencyService currencyService;

    @Override
    public Currency parseCurrency(List<Currency> currencies, Integer code, String name) {
        for (Currency currency : currencies) {
            if (Objects.equals(currency.getCurrencyCode(), code)) {
                return currency;
            }
        }
        Currency currency = currencyService.saveCurrency(Currency.builder()
                .currencyCode(code)
                .currencyName(name).build());
        currencies.add(currency);
        return currency;
    }
}
