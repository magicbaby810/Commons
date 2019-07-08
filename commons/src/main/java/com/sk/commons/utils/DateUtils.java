package com.sk.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @desc 时间格式转换工具
 *
 * @author sk on 2016-12-19.
 */

public class DateUtils {

    private String fetchTimeStr;
    private String dateStr;
    private String timeStr;
    private String hourPosition;
    private String minutePosition;
    private int hour;
    private int minute;

    public DateUtils() {
        fetchTimeStr = getFullDateText(System.currentTimeMillis());
        dateStr = fetchTimeStr.split(" ")[0];
        timeStr = fetchTimeStr.split(" ")[1];
        hourPosition = timeStr.split(":")[0];
        minutePosition = timeStr.split(":")[1];
        hour = Integer.parseInt(minutePosition) > 30 ? Integer.parseInt(hourPosition) + 1 : Integer.parseInt(hourPosition);
        minute = calculateMinute(Integer.parseInt(minutePosition));
    }

    public String getFetchTimeStr() {
        return dateStr + " " + hour + ":" + (minute*15 == 0 ? "00" : minute*15);
    }

    private int calculateMinute(int minute) {
        if (0 < minute && minute <= 15) {
            return 2;
        } else if (minute <= 30) {
            return 3;
        } else if (minute <= 45) {
            return 0;
        } else if (minute <= 60) {
            return 1;
        }
        return 0;
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getHourPosition() {
        return hourPosition;
    }

    public String getMinutePosition() {
        return minutePosition;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public static long getTimeStamp( SimpleDateFormat format, String dateStr){
        try {
            Date date = format.parse(dateStr);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTomorrowDateText(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        String date = format.format(c.getTimeInMillis());
        return date;
    }

    public static String getSimpleDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = format.format(time);
        return date;
    }
    public static String getSimpleDateText2(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getTimeText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getHalfDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getSimpleHalfDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getFullPointDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getFullDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getFullChineseDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getFullSSDateText(Long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String date = format.format(time);
        return date;
    }

    public static String getSimpleFormat(long time) {
        String format = getTimeText(time);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        if (getTodayofDays() == c.get(Calendar.DAY_OF_WEEK) - 1) {
            return "今天" + format;
        } else if (getTodayofDays() == c.get(Calendar.DAY_OF_WEEK) - 2) {
            return "明天" + format;
        } else if (getTodayofDays() == c.get(Calendar.DAY_OF_WEEK) - 3) {
            return "后天" + format;
        }
        return "  "+ format;
    }
    public static String getSimpleFormat2(long time) {
        String format = getTimeText(time);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        if (getTodayofDays() == c.get(Calendar.DAY_OF_WEEK) - 1) {
            return "今天" + format;
        } else if (getTodayofDays() == c.get(Calendar.DAY_OF_WEEK) - 2) {
            return "明天" + format;
        } else if (getTodayofDays() == c.get(Calendar.DAY_OF_WEEK) - 3) {
            return getSimpleHalfDateText(time);
        }
        return "  "+ format;
    }

    public static int getTodayofDays() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(format.format(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String getYearMonthDateText(String year, String month, String day){
        StringBuilder sb = new StringBuilder(year);
        if(month.length() == 1 && Integer.valueOf(month)<10) {
            sb.append("0"+month);
        }else {
            sb.append(month);
        }
        if(day.length() == 1 && Integer.valueOf(day)<10) {
            sb.append("0"+day);
        }else {
            sb.append(day);
        }

        return sb.toString();
    }

    /**
     * 日期格式字符串转换成时间戳
     * @param dateStr 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
            return String.valueOf(sdf.parse(dateStr).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
