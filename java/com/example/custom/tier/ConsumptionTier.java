package com.example.custom.tier;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Consumption;

import java.util.List;

public interface ConsumptionTier {

    List<Arrival> tie(List<Arrival> arrivals, List<Consumption> consumptions, Arrival arrival);
}
