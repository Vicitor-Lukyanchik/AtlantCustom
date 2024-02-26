package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SettingsDto {

    private boolean isOn;

    private List<String> time;

    private String lastUpdate;
}
