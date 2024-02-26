package com.example.custom.thread;

import com.example.custom.exception.ServiceException;
import com.example.custom.migration.DbfToDbMigrator;
import com.example.custom.reader.FileReader;
import com.example.custom.writer.FileWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AutomaticDataUpdater extends Thread {

    private static final String ENCODING = "utf-8";
    private static final String NEXT_LINE = "\n";
    private static final long MONTH = 2628000000L;
    private static final long WEEK = 604799337L;
    private static final long DAY = 86399905L;
    private static final long HOUR = 3599996L;
    private static final long MINUTE = 59999L;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
    private final FileWriter fileWriter;
    private final FileReader fileReader;
    private final DbfToDbMigrator migrator;
    private final String exceptionFilePath;
    private final String updateFilePath;

    private boolean isFirstTimeRun = true;

    @Override
    public void run() {
        if (isFirstTimeRun) {
            isFirstTimeRun = false;
            try {
                while (true) {
                    if (isChecked()) {
                        updateAllData();
                    }
                }
            } catch (Exception e) {
                String message = e.getMessage() + NEXT_LINE + " Failed (" + LocalDateTime.now().format(dateTimeFormatter) + ")\n";
                if (e.getMessage().equals("sleep interrupted")) {
                    message = "Restart (" + LocalDateTime.now().format(dateTimeFormatter) + ")\n";
                }
                fileWriter.writeAppendFile(exceptionFilePath, message, ENCODING);
            }
        }
    }

    private boolean isChecked() throws InterruptedException {
        boolean result = true;
        long timeout = WEEK;
        try {
            String readFileText = fileReader.readFile(updateFilePath, "utf-8");
            List<String> settings = Arrays.stream(readFileText.split(NEXT_LINE)).filter(s -> !s.contains("//") && !s.isEmpty()).collect(Collectors.toList());

            for (String setting : settings) {
                String[] property = setting.split("=");
                if (property[0].equals("auto_update_data")) {
                    result = property[1].contains("on");
                } else {
                    timeout = getTimeout(property[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Can't find file with path:" + updateFilePath + "| recommend restart server, program will create");
            fileWriter.writeAppendFile(exceptionFilePath, "Can't find file with path:" + updateFilePath + "| recommend restart server, program will create (" + LocalDateTime.now().format(dateTimeFormatter) + ")\n", ENCODING);
        }

        sleep(timeout);
        return result;
    }

    private static long getTimeout(String result) {
        if (result.contains("month")) {
            return MONTH;
        } else if (result.contains("day")) {
            return DAY;
        } else if (result.contains("hour")) {
            return HOUR;
        } else if (result.contains("minute")) {
            return MINUTE;
        } else if (result.matches("[0-9]+")) {
            return Long.parseLong(result);
        }
        return WEEK;
    }

    private void updateAllData() {
        migrator.migrateLastUpdate();
        fileWriter.writeAppendFile(exceptionFilePath, "Success (" + LocalDateTime.now().format(dateTimeFormatter) + ")" + NEXT_LINE, ENCODING);
    }
}
