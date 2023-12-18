package com.example.custom.security;

public interface NumberRequestProvider {

    void add(String token, String number);

    String take(String token);
}
