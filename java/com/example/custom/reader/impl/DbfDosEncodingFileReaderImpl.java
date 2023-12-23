package com.example.custom.reader.impl;

import com.example.custom.dto.DbfDto;
import com.example.custom.exception.DbfReaderException;
import com.example.custom.reader.DbfDosEncodingFileReader;
import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbfDosEncodingFileReaderImpl implements DbfDosEncodingFileReader {

    private static final String DOS_ENCODING = "CP866";

    @Override
    public DbfDto read(String path) {
        List<String> columns;
        List<List<String>> values;

        try (InputStream fis = new FileInputStream(path)) {
            DBFReader reader = new DBFReader(fis);
            reader.setCharactersetName(DOS_ENCODING);

            columns = getColumnsNames(reader);
            values = getValues(reader);
        } catch (Exception e) {
            throw new DbfReaderException("Can't read dbf file with path '" + path + "' \n" + e.getMessage());
        }

        return DbfDto.builder().columns(columns).objects(values).build();
    }

    private List<List<String>> getValues(DBFReader reader) throws DBFException {
        List<List<String>> result = new ArrayList<>();
        Object[] rowValues;

        while ((rowValues = reader.nextRecord()) != null) {
            List<String> dbfObject = new ArrayList<>();
            for (Object rowValue : rowValues) {
                dbfObject.add(String.valueOf(rowValue).trim());
            }
            result.add(dbfObject);
        }
        return result;
    }

    private List<String> getColumnsNames(DBFReader reader) throws DBFException {
        List<String> result = new ArrayList<>();
        int fieldsCount = reader.getFieldCount();

        for (int i = 0; i < fieldsCount; i++) {
            DBFField field = reader.getField(i);
            result.add(field.getName());
        }
        return result;
    }
}
