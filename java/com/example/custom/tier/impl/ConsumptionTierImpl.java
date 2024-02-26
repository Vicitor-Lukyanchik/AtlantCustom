package com.example.custom.tier.impl;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Consumption;
import com.example.custom.exception.TierException;
import com.example.custom.tier.ConsumptionTier;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConsumptionTierImpl implements ConsumptionTier {

    public List<Arrival> tie(List<Arrival> arrivals, List<Consumption> consumptions, Arrival NonArrivals) {
        List<Arrival> result = new ArrayList<>();

        while (!arrivals.isEmpty()) {
            List<Arrival> foundArrivals = findArrivalsByParameters(arrivals, arrivals.get(0)).stream()
                    .sorted(Comparator.comparing(Arrival::getArrivalDate))
                    .collect(Collectors.toList());

            List<Consumption> foundConsumptions = findConsumptionsByParameters(consumptions, arrivals.get(0)).stream()
                    .sorted(Comparator.comparing(Consumption::getArrivalDate))
                    .collect(Collectors.toList());

            result.addAll(buildArrivals(deleteArrivalsCorrectives(foundArrivals),
                    deleteConsumptionsCorrectives(foundConsumptions), NonArrivals));

            consumptions.removeAll(foundConsumptions);
            arrivals.removeAll(foundArrivals);

        }

        result.add(buildNonArrivalConsumptions(consumptions, NonArrivals));
        return result;
    }

    private List<Arrival> findArrivalsByParameters(List<Arrival> arrivals, Arrival byArrival) {
        List<Arrival> result = new ArrayList<>();
        for (Arrival arrival : arrivals) {
            try {
                if (arrival.getProductName().equals(byArrival.getProductName()) && arrival.getProductPrice().equals(byArrival.getProductPrice()) &&
                        arrival.getUnit().equals(byArrival.getUnit()) && arrival.getStorehouseNumber().equals(byArrival.getStorehouseNumber())) {
                    result.add(arrival);
                }
            } catch (Exception e) {
                throw new TierException("Некорректный приход (название|цена|еденица измерения|номер склада)");
            }
        }
        return result;
    }

    private List<Consumption> findConsumptionsByParameters(List<Consumption> consumptions, Arrival byArrival) {
        List<Consumption> result = new ArrayList<>();
        for (Consumption consumption : consumptions) {
            try {
                if (consumption.getProductName().equals(byArrival.getProductName()) && consumption.getProductPrice().equals(byArrival.getProductPrice()) &&
                        consumption.getUnit().equals(byArrival.getUnit()) && consumption.getStorehouseNumber().equals(byArrival.getStorehouseNumber())) {
                    result.add(consumption);
                }
            } catch (Exception e) {
                throw new TierException("Некорректный расход (название|цена|еденица измерения|номер склада)");
            }
        }
        return result;
    }

    private List<Arrival> buildArrivals(List<Arrival> arrivals, List<Consumption> consumptions, Arrival nonArrival) {
        List<Arrival> result = new ArrayList<>();
        int i = 0;
        double lastCount = 0;
        double freedomConsumptionCount = 0;

        for (Arrival arrival : arrivals) {
            if (arrival.getConsumptions() == null) {
                arrival.setConsumptions(new ArrayList<>());
            }
            boolean isNext = true;
            double freedomArrivalCount = calculateFreedomArrivalCount(arrival);
            i = checkDate(consumptions, i, arrival.getArrivalDate(), nonArrival);


            while (isNext && i < consumptions.size()) {
                freedomConsumptionCount = consumptions.get(i).getTurnoverCount() - lastCount;
                isNext = freedomArrivalCount > freedomConsumptionCount;

                if (freedomArrivalCount < freedomConsumptionCount) {
                    arrival.getConsumptions().add(buildConsumption(consumptions.get(i), arrival, freedomArrivalCount));

                    lastCount += freedomArrivalCount;
                    freedomConsumptionCount -= freedomArrivalCount;
                } else {
                    arrival.getConsumptions().add(buildConsumption(consumptions.get(i), arrival, freedomConsumptionCount));

                    freedomArrivalCount -= freedomConsumptionCount;
                    freedomConsumptionCount = 0;
                    lastCount = 0;
                    i++;
                }
            }
            result.add(arrival);
        }
        if (!result.isEmpty()) {
            addAllNonArrivalConsumptions(consumptions, i, result.get(result.size() - 1), nonArrival, freedomConsumptionCount);
        }
        return result;
    }

    private double calculateFreedomArrivalCount(Arrival arrival) {
        double busy = .0;
        for (Consumption consumption : arrival.getConsumptions()) {
            busy += consumption.getTurnoverCount();
        }
        return arrival.getTurnoverCount() - busy;
    }

    private int checkDate(List<Consumption> consumptions, int i, LocalDate date, Arrival nonArrival) {
        while (i < consumptions.size()) {
            try {
                if (consumptions.get(i).getArrivalDate().isAfter(date) || consumptions.get(i).getArrivalDate().isEqual(date)) {
                    return i;
                }
            } catch (Exception e) {
                throw new TierException("Problem with date");
            }
            nonArrival.getConsumptions().add(consumptions.get(i));
            i++;
        }
        return i;
    }

    private void addAllNonArrivalConsumptions(List<Consumption> consumptions, int i, Arrival lastArrival, Arrival nonArrival, double freedomConsumptionCount) {
        if (lastArrival.getConsumptions() == null) {
            lastArrival.setConsumptions(new ArrayList<>());
        }
        if (i < consumptions.size()) {
            if (freedomConsumptionCount > 0) {
                if (freedomConsumptionCount > 0.0001) {
                    nonArrival.getConsumptions().add(buildConsumption(consumptions.get(i), nonArrival, freedomConsumptionCount));
                }
                i++;
            }
            while (i < consumptions.size()) {
                nonArrival.getConsumptions().add(buildConsumption(consumptions.get(i), nonArrival, consumptions.get(i).getTurnoverCount()));
                i++;
            }
        }

    }

    private Consumption buildConsumption(Consumption consumption, Arrival arrival, double count) {
        DecimalFormat twoDForm = new DecimalFormat("#.####");
        Double resultCount = Double.valueOf(twoDForm.format(count).replace(",", "."));
        return consumption.toBuilder().turnoverCount(resultCount).arrival(arrival).build();
    }


    private Arrival buildNonArrivalConsumptions(List<Consumption> consumptions, Arrival arrival) {
        if (arrival.getConsumptions() == null || arrival.getConsumptions().isEmpty()) {
            arrival.setConsumptions(new ArrayList<>());
        }

        while (!consumptions.isEmpty()) {
            Arrival arrivalConsumptionsFound = Arrival.builder()
                    .productName(consumptions.get(0).getProductName()).productPrice(consumptions.get(0).getProductPrice())
                    .unit(consumptions.get(0).getUnit()).storehouseNumber(consumptions.get(0).getStorehouseNumber()).build();

            List<Consumption> foundConsumptions = findConsumptionsByParameters(consumptions, arrivalConsumptionsFound).stream()
                    .sorted(Comparator.comparing(Consumption::getArrivalDate))
                    .collect(Collectors.toList());

            List<Consumption> deletedConsumptionsCorrectives = deleteConsumptionsCorrectives(foundConsumptions);
            arrival.getConsumptions().addAll(deletedConsumptionsCorrectives);
            for (Consumption foundConsumption : deletedConsumptionsCorrectives) {
                foundConsumption.setArrival(arrival);
            }

            consumptions.removeAll(foundConsumptions);
        }
        return arrival;
    }

    private List<Arrival> deleteArrivalsCorrectives(List<Arrival> arrivals) {
        List<Integer> deletedArrivals = new ArrayList<>();

        for (int i = 0; i < arrivals.size(); i++) {
            deletedArrivals.addAll(findDeletedArrivals(arrivals, i));
        }

        List<Arrival> result = new ArrayList<>();
        for (int j = 0; j < arrivals.size(); j++) {
            if (isDeleted(deletedArrivals, j)) {
                result.add(arrivals.get(j));
            }
        }
        return result;
    }

    private List<Integer> findDeletedArrivals(List<Arrival> arrivals, int i) {
        List<Integer> deletedArrivals = new ArrayList<>();
        if (arrivals.get(i).getTurnoverCount() < 0) {
            deletedArrivals.add(i);
            deletedArrivals.addAll(findReciprocalArrivalCount(arrivals, i));
        }
        return deletedArrivals;
    }

    private List<Integer> findReciprocalArrivalCount(List<Arrival> arrivals, int i) {
        for (int j = 0; j < arrivals.size(); j++) {
            if (arrivals.get(j).getTurnoverCount() + arrivals.get(i).getTurnoverCount() == 0) {
                return new ArrayList<>(List.of(j));
            }
        }
        return new ArrayList<>();
    }

    private List<Consumption> deleteConsumptionsCorrectives(List<Consumption> consumptions) {
        List<Integer> deletedConsumptions = new ArrayList<>();

        for (int i = 0; i < consumptions.size(); i++) {
            deletedConsumptions.addAll(findDeletedConsumption(consumptions, i, deletedConsumptions));
        }

        List<Consumption> result = new ArrayList<>();
        for (int j = 0; j < consumptions.size(); j++) {
            if (isDeleted(deletedConsumptions, j)) {
                result.add(consumptions.get(j));
            }
        }
        return result;
    }

    private List<Integer> findDeletedConsumption(List<Consumption> consumptions, int i, List<Integer> deletedConsumptions) {
        List<Integer> result = new ArrayList<>();
        if (consumptions.get(i).getTurnoverCount() < 0) {
            result.add(i);
            result.addAll(findReciprocalConsumptionCount(consumptions, i, deletedConsumptions));
        }
        return result;
    }

    private List<Integer> findReciprocalConsumptionCount(List<Consumption> consumptions, int i, List<Integer> deletedConsumptions) {
        for (int j = 0; j < consumptions.size(); j++) {
            if (consumptions.get(j).getTurnoverCount() + consumptions.get(i).getTurnoverCount() == 0 && consumptions.get(j).getId() == null
                    && !deletedConsumptions.contains(j)) {
                return new ArrayList<>(List.of(j));
            }
        }
        for (int j = 0; j < consumptions.size(); j++) {
            if (consumptions.get(j).getTurnoverCount() + consumptions.get(i).getTurnoverCount() == 0 && !deletedConsumptions.contains(j)) {
                return new ArrayList<>(List.of(j));
            }
        }
        return new ArrayList<>();
    }

    private static boolean isDeleted(List<Integer> deletedObjects, int j) {
        for (Integer deleted : deletedObjects) {
            if (deleted == j) {
                return false;
            }
        }
        return true;
    }
}