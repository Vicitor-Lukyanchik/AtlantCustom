package com.example.custom.service;

import com.example.custom.dto.SettingsDto;

import java.util.List;

public interface SettingsService {

    SettingsDto findSettings();

    void updateSettings(boolean isOn, String timeout);
}
