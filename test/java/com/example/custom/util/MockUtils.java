package com.example.custom.util;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Consumption;
import com.example.custom.entity.Unit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockUtils {

    private static final String NON_ARRIVAL_NAME = "NonArrivalsConsumptions";

    public static Arrival createNonArrival() {
        return Arrival.builder().productName(NON_ARRIVAL_NAME).turnoverCount(-.1).consumptions(new ArrayList<>()).build();
    }

    public static List<Arrival> createArrivals() {
        return new ArrayList<>(List.of(
                Arrival.builder().productName("1").turnoverCount(3.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(1)).storehouseNumber("70").unit(createUnit("штуки")).consumptions(new ArrayList<>()).build(),
                Arrival.builder().productName("2").turnoverCount(2.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(2)).storehouseNumber("70").unit(createUnit("штуки")).consumptions(new ArrayList<>()).build(),
                Arrival.builder().productName("3").turnoverCount(1.5).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(3)).storehouseNumber("70").unit(createUnit("штуки")).consumptions(new ArrayList<>()).build()));
    }

    public static List<Consumption> createConsumptions() {
        return new ArrayList<>(List.of(
                Consumption.builder().productName("1").turnoverCount(2.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(1)).storehouseNumber("70").unit(createUnit("штуки")).build(),
                Consumption.builder().productName("1").turnoverCount(1.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(1)).storehouseNumber("70").unit(createUnit("штуки")).build(),
                Consumption.builder().productName("2").turnoverCount(2.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(2)).storehouseNumber("70").unit(createUnit("штуки")).build(),
                Consumption.builder().productName("3").turnoverCount(.5).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(3)).storehouseNumber("70").unit(createUnit("штуки")).build(),
                Consumption.builder().productName("3").turnoverCount(1.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(3)).storehouseNumber("70").unit(createUnit("штуки")).build()));
    }

    public static List<Arrival> createReadyArrivals() {
        return new ArrayList<>(List.of(
                Arrival.builder().productName("1").turnoverCount(3.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(1)).storehouseNumber("70").unit(createUnit("штуки"))
                        .consumptions(new ArrayList<>(List.of(
                                Consumption.builder().productName("1").turnoverCount(2.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(1)).storehouseNumber("70").unit(createUnit("штуки")).build(),
                                Consumption.builder().productName("1").turnoverCount(1.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(1)).storehouseNumber("70").unit(createUnit("штуки")).build()
                        ))).build(),
                Arrival.builder().productName("2").turnoverCount(2.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(2)).storehouseNumber("70").unit(createUnit("штуки"))
                        .consumptions(new ArrayList<>(List.of(
                                Consumption.builder().productName("2").turnoverCount(2.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(2)).storehouseNumber("70").unit(createUnit("штуки")).build()
                        ))).build(),
                Arrival.builder().productName("3").turnoverCount(1.5).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(3)).storehouseNumber("70").unit(createUnit("штуки"))
                        .consumptions(new ArrayList<>(List.of(
                                Consumption.builder().productName("3").turnoverCount(.5).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(3)).storehouseNumber("70").unit(createUnit("штуки")).build(),
                                Consumption.builder().productName("3").turnoverCount(1.).arrivalDate(LocalDate.now()).productPrice(new BigDecimal(3)).storehouseNumber("70").unit(createUnit("штуки")).build()
                        ))).build()));
    }

    public static Arrival createArrival(String name, double count) {
        return Arrival.builder().productName(name).turnoverCount(count).arrivalDate(LocalDate.now()).unit(createUnit("штуки")).consumptions(new ArrayList<>()).productPrice(new BigDecimal(3)).storehouseNumber("70").build();
    }

    public static Arrival createArrival(String name, double count, List<Consumption> consumptions) {
        return Arrival.builder().productName(name).turnoverCount(count).arrivalDate(LocalDate.now()).unit(createUnit("штуки")).consumptions(consumptions).productPrice(new BigDecimal(3)).storehouseNumber("70").build();
    }

    public static Arrival createArrival(String name, double count, double price, String storehouse) {
        return Arrival.builder().productName(name).turnoverCount(count).arrivalDate(LocalDate.now()).unit(createUnit("штуки")).consumptions(new ArrayList<>()).productPrice(new BigDecimal(price)).storehouseNumber(storehouse).build();
    }

    public static Arrival createArrival(String name, double count, double price, String storehouse, LocalDate date) {
        return Arrival.builder().productName(name).turnoverCount(count).arrivalDate(date).unit(createUnit("штуки")).consumptions(new ArrayList<>()).productPrice(new BigDecimal(price)).storehouseNumber(storehouse).build();
    }

    public static Consumption createConsumption(String name, double count) {
        return Consumption.builder().productName(name).turnoverCount(count).arrivalDate(LocalDate.now()).unit(createUnit("штуки")).productPrice(new BigDecimal(3)).storehouseNumber("70").build();
    }

    public static Consumption createConsumption(String name, double count, double price, String storehouse) {
        return Consumption.builder().productName(name).turnoverCount(count).arrivalDate(LocalDate.now()).unit(createUnit("штуки")).productPrice(new BigDecimal(price)).storehouseNumber(storehouse).build();
    }

    public static Consumption createConsumption(String name, double count, double price, String storehouse, LocalDate date) {
        return Consumption.builder().productName(name).turnoverCount(count).arrivalDate(date).productPrice(new BigDecimal(price)).unit(createUnit("штуки")).storehouseNumber(storehouse).build();
    }

    public static Unit createUnit(String name) {
        return Unit.builder().unitName(name).build();
    }
}
