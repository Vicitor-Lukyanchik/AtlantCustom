package com.example.custom.writer;

public interface FileWriter {

    void writeFile(String newFilePath, String text, String encoding);

    void writeAppendFile(String newFilePath, String text, String encoding);
}
