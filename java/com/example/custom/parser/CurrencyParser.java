package com.example.custom.parser;

import com.example.custom.entity.Currency;

import java.util.List;

public interface CurrencyParser {

    Currency parseCurrency(List<Currency> currencies, Integer code, String name);
}
