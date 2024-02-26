package com.example.custom.writer.impl;

import com.example.custom.exception.FileCopierException;
import com.example.custom.writer.FileWriter;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileWriterImpl implements FileWriter {

    @Override
    public void writeFile(String newFilePath, String text, String encoding) {

        try {
            File directory = new File(newFilePath);

            if (!directory.exists()) {
                directory.getParentFile().mkdir();
            }

            try (OutputStream outputStream = new FileOutputStream(newFilePath)) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, encoding);
                if (encoding.contains("866")) {
                    text = text.replaceAll("№", "N");
                }
                outputStreamWriter.write(text);
                outputStreamWriter.close();
            }
        } catch (IOException e) {
            throw new FileCopierException("Not wrote to path=\'" + newFilePath + "\"");
        }
    }

    @Override
    public void writeAppendFile(String newFilePath, String text, String encoding) {

        try {
            File directory = new File(newFilePath);

            if (!directory.exists()) {
                directory.getParentFile().mkdir();
            }

            try (OutputStream outputStream = new FileOutputStream(newFilePath, true)) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, encoding);
                if (encoding.contains("866")) {
                    text = text.replaceAll("№", "N");
                }
                outputStreamWriter.write(text);
                outputStreamWriter.close();
            }
        } catch (IOException e) {
            throw new FileCopierException("Not wrote to path=\'" + newFilePath + "\"");
        }
    }
}