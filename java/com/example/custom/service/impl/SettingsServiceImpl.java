package com.example.custom.service.impl;

import com.example.custom.dto.SettingsDto;
import com.example.custom.reader.FileReader;
import com.example.custom.service.SettingsService;
import com.example.custom.thread.ThreadStarter;
import com.example.custom.writer.FileWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {


    private static final String AUTO_UPDATE_SETTINGS_TEXT = "auto_update_data=";
    private static final String PARTIAL_RENEWAL_SETTINGS_TEXT = "\npartial_renewal=";
    private static final String DESCRIPTION_SETTINGS_TEXT = "\n" + "\n" +
            "//Настройки:\n" + "//auto_update_data\n" + "//on | off\n" + "//partial_renewal:\n" +
            "//Месяц=month\n" + "//Неделя=week\n" + "//День=day\n" + "//Час=hour\n" + "//Минута=minute\n" +
            "//Если время конкретное, пишите числом в милисекундах(1 секунда = 1000 мсек)";
    private static final String ENCODING = "utf-8";
    private static final String LEFT = "\\(";
    private static final String RIGHT = "\\)";
    private static final String DEFAULT_TIME = "--.--.---- --:--";
    private static final String NEXT_LINE = "\n";
    private final FileReader fileReader;
    private final FileWriter fileWriter;
    private final ThreadStarter threadStarter;


    private static final List<String> TIMES = Arrays.asList("month", "week", "day", "hour", "minute");
    @Value(value = "${settings.update.file.path}")
    private String updateFilePath;

    @Value(value = "${stacktrace.data.update.file.path}")
    private String stackTrace;

    @Override
    public SettingsDto findSettings() {
        boolean result = true;
        List<String> resultTimes = new ArrayList<>();
        String lastUpdate = DEFAULT_TIME;

        try {
            String readFileText = fileReader.readFile(updateFilePath, "utf-8");
            List<String> settings = Arrays.stream(readFileText.split(NEXT_LINE)).filter(s -> !s.contains("//") && !s.isEmpty()).collect(Collectors.toList());

            for (String setting : settings) {
                String[] property = setting.split("=");
                if (property[0].equals("auto_update_data")) {
                    result = property[1].contains("on");
                } else {
                    String time = property[1];
                    resultTimes.add(time);

                    for (String timeout : TIMES) {
                        if (!time.equals(timeout)) {
                            resultTimes.add(timeout);
                        }
                    }

                }
            }

            readFileText = fileReader.readFile(stackTrace, "utf-8");
            List<String> stackTraces = Arrays.stream(readFileText.split(NEXT_LINE)).filter(s -> !s.contains("//") && !s.isEmpty()).collect(Collectors.toList());
            lastUpdate = buildLastUpdate(stackTraces);
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }

        return SettingsDto.builder().lastUpdate(lastUpdate).isOn(result).time(resultTimes).build();
    }

    private String buildLastUpdate(List<String> stackTraces) {
        int successUpdate = -1;
        for (int i = 0; i < stackTraces.size(); i++) {
            if (stackTraces.get(i).contains("Success")) {
                successUpdate = i;
            }
        }
        if (successUpdate < 0) {
            return DEFAULT_TIME;
        }
        return stackTraces.get(successUpdate).split(LEFT)[1].split(RIGHT)[0];
    }


    @Override
    public void updateSettings(boolean isOn, String timeout) {
        String choose = "off";
        if (isOn) {
            choose = "on";
        }

        String text = AUTO_UPDATE_SETTINGS_TEXT + choose + PARTIAL_RENEWAL_SETTINGS_TEXT + timeout + DESCRIPTION_SETTINGS_TEXT;
        fileWriter.writeFile(updateFilePath, text, ENCODING);
        threadStarter.restartThread();
    }
}
