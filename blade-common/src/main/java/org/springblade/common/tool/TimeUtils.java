package org.springblade.common.tool;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class TimeUtils {
    /**
     * 将日期格式转换为字符串格式利用JodaTime
     */
    public static String dateToStr(Date date){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.toString(dateTimeFormatter);
    }
    /**
     * 将日期格式转换为指定字符串格式利用JodaTime
     */
    public static String dateToStr(Date date,String formatter){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatter);
        return dateTime.toString(dateTimeFormatter);
    }


    public static Date strToDate(String dateTimeStr, String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

//    public static void main(String[] args) {
//        Date date = TimeUtils.strToDate("2010-01-01 11:11:11");
//        System.out.println(date);
//        Date now = null;
//        String s = TimeUtils.dateToStr(now);
//        System.out.println(s);
//    }

}
