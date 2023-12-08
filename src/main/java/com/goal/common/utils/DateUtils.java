package com.goal.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateUtils {
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String ISO_DATE_TIME_FORMAT_CUSTOM = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String SIMPLE_DATE_FORMAT = "dd-MMM-yyyy";

    public static final String SIMPLE_DATE_FORMAT_TO_SECOND_SIMPLE = "dd-MM-yyyy HH:mm:ss";

    public static final String SIMPLE_DATE_FORMAT_TO_SECOND = "dd/MM/yyyy HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT_TO_DAY = "dd/MM/yyyy";
    public static final String SIMPLE_DATE_FORMAT_TO_MONTH = "MM/dd/yyyy";
    public static final String SIMPLE_DATE_FORMAT_SOURCING_UPLOAD = "dd-MMM-yyyy";
    public static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_WITH_ALPHABET_MONTH_REGEX = "^([1-9]|[12]\\d|3[01])[-/.]([A-Z][a-z]{2}[-/.])(19|20)\\d{2}$";

    public static String formatDate(Date date, String format) {
        DateFormat df = getDateFormat(format);
        return isNotEmpty(date) ? df.format(date) : "";
    }

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

    public static boolean isNotEmpty(Object data) {
        return !isEmpty(data);
    }

    public static boolean isEmpty(Object data) {
        return data == null;
    }

    public static DateFormat getDateFormat(String format) {
        return strictDateFormatForPattern(format);
    }

    private static DateFormat strictDateFormatForPattern(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        return dateFormat;
    }

    public static boolean validateDateFormat(String dateString) {
        if (CommonDataUtil.isEmpty(dateString)) {
            return false;
        }
        Pattern pattern = Pattern.compile(DATE_WITH_ALPHABET_MONTH_REGEX);
        return pattern.matcher(dateString).matches();
    }

    public static Date formatDateTime(String dateStr) {
        String format = DateUtils.validateDateFormat(dateStr) ? DateUtils.SIMPLE_DATE_FORMAT : DateUtils.ISO_DATE_TIME_FORMAT;
        return DateUtils.parseDateString(dateStr, format);
    }


    public static LocalDate convertStringLocalDate(String strDate) {
        String inputPattern = "MMM dd,yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            String strCurrentDate = strDate;
            date = inputFormat.parse(strCurrentDate);// it's format should be same as inputPattern
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateOutput = DateUtils.parseDateString(str, SIMPLE_DATE_FORMAT_SOURCING_UPLOAD);
        return dateOutput.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }



        public static String convertStringLocalDate_Search (String strDate){
            Date date = DateUtils.parseDateString(strDate, SIMPLE_DATE_FORMAT_TO_MONTH);
            LocalDateTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String result = time.toString();
            return result;

        }
    public static LocalDateTime convertStringLocalDateTime(String strDate){
        Date date = DateUtils.parseDateString(strDate, STANDARD_DATE_TIME_FORMAT);
        LocalDateTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return  time;

    }

        public static Date convertStringToDate (String dateString){
            try {
                // Add 12:00:00 because if not date will save back 1 day
                dateString = dateString + " 12:00:00";
                SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT_TO_SECOND, Locale.ENGLISH);
                Date date = formatter.parse(dateString);
                return date;
            } catch (ParseException e) {
                return null;
            }

        }

        public static Instant convertStringToInstant (String dateString){
            // Add 12:00:00 because if not date will save back 1 day
            dateString = dateString + " 12:00:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT_TO_SECOND_SIMPLE);

            LocalDateTime localDate = LocalDateTime.parse(dateString, formatter);
            Instant instant = localDate.atZone(ZoneId.systemDefault()).toInstant();
            return instant;
        }

        public static Date convertStringToInstantDate (String dateString){

            return DateUtils.parseDateString(dateString, ISO_DATE_TIME_FORMAT_CUSTOM);

        }


        public static String convertInstantToDate (Instant dateStr){
            SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT_TO_DAY, Locale.ENGLISH);
            Date date = Date.from(dateStr);
            String formattedDate = formatter.format(date);
            return formattedDate;

        }

        public static StopWatch initStopWatch () {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            return stopWatch;
        }

        public static String calculateTime (StopWatch stopWatch){
            stopWatch.stop();
            String pattern = "mm:ss:SSS";
            Date date = new Date(stopWatch.getTotalTimeMillis());
            Format format = new SimpleDateFormat(pattern);
            return format.format(date) + " [mm:ss:SSS]";
        }

        public static Instant currentInstant() {
            return new Date().toInstant();
        }
    }
