package com.zlagoda.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private MessageSource messageSource;


    public LocalDateTimeFormatter() {
        super();
    }

    public LocalDateTime parse(final String text, final Locale locale) throws ParseException {
        return LocalDateTime.parse(text, formatter.localizedBy(locale));
    }

    public String print(final LocalDateTime object, final Locale locale) {
        return formatter.localizedBy(locale).format(object);
    }

    private SimpleDateFormat createDateFormat(final Locale locale) {
        final String format = this.messageSource.getMessage("date.format", null, locale);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        return dateFormat;
    }

}