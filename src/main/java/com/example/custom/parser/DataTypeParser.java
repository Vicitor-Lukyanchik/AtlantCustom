package com.example.custom.parser;

import com.example.custom.exception.ParserException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DataTypeParser {

    private static final Integer IMPORT_VALUE = 1;
    private static final String BAD_DATE = "null";
    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String SPACE = " ";

    private static final DateFormat dateFormat = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime parseLocalDateTime(String date, String time) {
        LocalDateTime result = null;
        if (!date.equals(BAD_DATE) || !time.equals(BAD_DATE)) {
            try {
                String formattedDate = parseToDate(date).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
                result = LocalDateTime.parse(formattedDate + SPACE + time, dateTimeFormatter);
            } catch (Exception e) {
                throw new ParserException("There is problem with date:" + date);
            }
        }
        return result;
    }

    public static LocalDate parseToDate(String date) {
        LocalDate result = null;
        if (!date.equals(BAD_DATE)) {
            try {
                result = dateFormat.parse(date).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } catch (ParseException e) {
                throw new ParserException("There is problem with date:" + date);
            }
        }
        return result;
    }

    public static Boolean isImport(int integer) {
        return IMPORT_VALUE == integer;
    }

    public static Long parseLong(String number) {
        return new BigDecimal(number).longValue();
    }

    public static Double parseDouble(String number) {
        return new BigDecimal(number).doubleValue();
    }

    public static Integer parseInteger(String number) {
        return new BigDecimal(number).intValue();
    }

}
