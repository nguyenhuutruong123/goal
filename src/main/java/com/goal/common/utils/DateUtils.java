package com.goal.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String SIMPLE_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String DATE_WITH_ALPHABET_MONTH_REGEX = "^([1-9]|[12]\\d|3[01])[-/.]([A-Z][a-z]{2}[-/.])(19|20)\\d{2}$";

    public static Date parseDateString(String dateStr, String pattern) {

        if (StringUtils.isNotBlank(dateStr) && StringUtils.isNotBlank(pattern)) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                return df.parse(dateStr);
            } catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    public static boolean validateDateFormat(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return false;
        }
        Pattern pattern = Pattern.compile(DATE_WITH_ALPHABET_MONTH_REGEX);
        return pattern.matcher(dateString).matches();
    }

    public static Date formatDateTime(String dateStr) {
        String format = DateUtils.validateDateFormat(dateStr) ? DateUtils.SIMPLE_DATE_FORMAT : DateUtils.ISO_DATE_TIME_FORMAT;
        return DateUtils.parseDateString(dateStr, format);
    }
}
