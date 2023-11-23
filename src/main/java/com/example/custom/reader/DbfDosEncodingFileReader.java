package com.example.custom.reader;

import com.example.custom.dto.DbfDto;
import com.example.custom.exception.DbfReaderException;

public interface DbfDosEncodingFileReader {

    DbfDto read(String path) throws DbfReaderException;
}
