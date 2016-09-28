package com.example.think.waterworksapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Think on 2016/9/26.
 */

public class DateFormatUtlis {


    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_ANALYSIS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.000+0000";
    private static final SimpleDateFormat analysisSimpleDateFormat = new SimpleDateFormat(DEFAULT_ANALYSIS_DATE_FORMAT);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    public static String format(String dateStr){
        try {
            Date date = analysisSimpleDateFormat.parse(dateStr);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            return "时间未知";
        }
    }

    public static String simpleFormat(String dateStr,SimpleDateFormat analysisSimpleDateFormat,SimpleDateFormat simpleDateFormat){
        try {
            Date date = analysisSimpleDateFormat.parse(dateStr);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            return "时间未知";
        }
    }

    public static String simpleFormat(String dateStr,SimpleDateFormat simpleDateFormat){
        try {
            Date date = analysisSimpleDateFormat.parse(dateStr);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            return "时间未知";
        }
    }


}
