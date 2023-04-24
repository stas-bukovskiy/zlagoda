package com.zlagoda.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");


    public static String convertToDateString(Date date) {
        return dateFormat.format(date);
    }

    public static Date convertToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Timestamp convertToTimestamp(String text) {
        try {
            return new Timestamp(formatter.parse(text).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}