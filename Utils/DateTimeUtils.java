package com.example.tmp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final SimpleDateFormat SDF_DEFAULT = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
    private static final SimpleDateFormat SDF_UTC = new SimpleDateFormat(UTC_FORMAT, Locale.getDefault());

    public static String getUnixTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String nowDefault() {
        return SDF_DEFAULT.format(new Date());
    }

    public static String nowUTC() {
        return SDF_UTC.format(new Date());
    }

    public static boolean isAfterDefault(String nowTime, String expireTime) {
        try {
            Date nowDate = SDF_DEFAULT.parse(nowTime);
            Date expireDate = SDF_DEFAULT.parse(expireTime);
            return nowDate.after(expireDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String defaultToUTC(String text){
        try {
            Date date = SDF_DEFAULT.parse(text);
            SDF_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            return SDF_UTC.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String utcToDefault(String text){
        try {
            SimpleDateFormat sdfFrom = new SimpleDateFormat(UTC_FORMAT, Locale.getDefault());
            sdfFrom.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat sdfTo = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
            return sdfTo.format(sdfFrom.parse(text));
        } catch (Exception e) {
            return text;
        }
    }
}
