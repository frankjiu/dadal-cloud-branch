package com.core.utils;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-11-05
 */
public class DateUtils {

    public static Date localDate2Date(LocalDate localDate) {
        if(null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate date2LocalDate(Date date) {
        if(null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date转为String
     * @param date
     * @param pattern("yyyyMMdd" 或 "yyyy-MM-dd"等)
     * @return
     */
    public static String date2LocalDateStr(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if(null == date) {
            return null;
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String localDateStr = localDate.format(formatter);
        return localDateStr;
    }

    public static String date2LocalDateFormatStr(Date date) {
        if(null == date) {
            return "";
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String localDateStr = localDate.format(formatters);
        return localDateStr;
    }

    public static String date2LocalDateTimeFormatStr(Date date) {
        if(null == date) {
            return "";
        }
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatters);
    }

    /**
     * String类型转LocalDate
     * @param strDate
     * @param pattern
     * @return
     */
    public static LocalDate string2LocalDate(String strDate, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate = LocalDate.parse(strDate, formatter);
        return localDate;
    }

    /**
     * String类型转LocalDateTime
     * @param strDate
     * @param pattern
     * @return
     */
    public static LocalDateTime string2LocalDateTime(String strDate, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(strDate, formatter);
        return localDateTime;
    }

    public static String formatterToSplit(String strDate) {
        Date date;
        String dateString = null;
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(strDate);
            dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * 时间类型 Long转String
     * @param date
     * @param pattern("yyyyMMdd" 或 "yyyy-MM-dd"等)
     * @return
     */
    public static String convertLongDateToString(Long date, String pattern){
        Assert.notNull(date, "the param date is null!");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(date),ZoneId.systemDefault()));
    }

}
