package com.example.custom.reader.impl;

import com.example.custom.exception.FileCopierException;
import com.example.custom.reader.FileReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class FileReaderImpl implements FileReader {

    @Override
    public String readFile(String filePath, String encoding) {

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, encoding))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append('\n');
                    }
                }
                return sb.toString();
        } catch (IOException e) {
            throw new FileCopierException("File by path=\"" + filePath + "\" not readed");
        }
    }
}