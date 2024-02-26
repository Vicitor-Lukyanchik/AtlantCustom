package com.example.custom.thread.impl;

import com.example.custom.migration.DbfToDbMigrator;
import com.example.custom.reader.FileReader;
import com.example.custom.thread.AutomaticDataUpdater;
import com.example.custom.thread.ThreadStarter;
import com.example.custom.writer.FileWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@RequiredArgsConstructor
public class ThreadStarterImpl implements ThreadStarter {

    private static final String SETTINGS_TEXT = "auto_update_data=on\n" + "partial_renewal=week\n" + "\n" +
            "//Настройки:\n" + "//auto_update_data\n" + "//on | off\n" + "//partial_renewal:\n" +
            "//Месяц=month\n" + "//Неделя=week\n" + "//День=day\n" + "//Час=hour\n" + "//Минута=minute\n" +
            "//Если время конкретное, пишите числом в милисекундах(1 секунда = 1000 мсек)";
    private static final String ENCODING = "utf-8";

    private static AutomaticDataUpdater updater;
    private final FileWriter fileWriter;
    private final FileReader fileReader;
    private final DbfToDbMigrator migrator;


    @Value(value = "${settings.update.file.path}")
    private String settingsPath;

    @Value(value = "${stacktrace.data.update.file.path}")
    private String exceptionFilePath;


    @PostConstruct
    public void startUpdateThread() {
        if (!new File(settingsPath).isFile() || fileReader.readFile(settingsPath, ENCODING).isEmpty()) {
            fileWriter.writeFile(settingsPath, SETTINGS_TEXT, ENCODING);
        }
        updater = new AutomaticDataUpdater(fileWriter,fileReader, migrator, exceptionFilePath, settingsPath);
        updater.start();
    }

    @Override
    public void restartThread() {
        if(updater.isAlive()){
            updater.interrupt();
        }
        updater = new AutomaticDataUpdater(fileWriter,fileReader, migrator, exceptionFilePath, settingsPath);
        updater.start();
    }
}
