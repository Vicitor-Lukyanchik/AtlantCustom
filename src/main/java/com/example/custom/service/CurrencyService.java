package com.example.custom.service;

import com.example.custom.entity.Currency;
import com.example.custom.entity.Unit;

import java.util.List;

public interface CurrencyService {

    List<Currency> getAllCurrencies();

    Currency saveCurrency(Currency currency);

    void deleteAll();
}
