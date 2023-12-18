package com.example.custom.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
        if (locDateTime != null) {
            return Timestamp.valueOf(locDateTime);
        }
        return null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        if(sqlTimestamp != null){
            return sqlTimestamp.toLocalDateTime();
        }
        return null;
    }
}
