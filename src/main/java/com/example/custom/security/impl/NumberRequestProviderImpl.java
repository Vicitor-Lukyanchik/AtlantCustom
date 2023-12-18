package com.example.custom.security.impl;

import com.example.custom.security.NumberRequestProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NumberRequestProviderImpl implements NumberRequestProvider {

    private Map<String, String> requestNumbers = new HashMap<>();

    @Override
    public void add(String token, String number) {
        requestNumbers.put(token, number);
    }

    @Override
    public String take(String token) {
        String result = requestNumbers.get(token);
        requestNumbers.remove(token);
        return result;
    }
}
