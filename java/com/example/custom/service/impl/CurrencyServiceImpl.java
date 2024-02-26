package com.example.custom.service.impl;

import com.example.custom.entity.Currency;
import com.example.custom.entity.Unit;
import com.example.custom.repository.CurrencyRepository;
import com.example.custom.repository.UnitRepository;
import com.example.custom.service.CurrencyService;
import com.example.custom.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    @Transactional
    public Currency saveCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public void deleteAll() {
        currencyRepository.deleteAll();
        currencyRepository.setAutoincrementOnStart();
    }
}
