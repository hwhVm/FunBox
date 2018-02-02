package com.beini.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by beini on 2018/2/2.
 * Date、String、Calendar类型之间的转化
 */

public class DateUtil {
    //private final String format = "yyyy-MM-dd HH:mm:ss";
    private final String format = "yyyy-MM-dd";

    /**
     * Calendar 转化 String
     *
     * @param calendat
     * @return
     */
    public String calendarToStr(Calendar calendat) {
        //Calendar calendat = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(calendat.getTime());
        return dateStr;
    }

    /**
     * String 转化Calendar
     *
     * @param str "2018-2-2";
     * @return
     */
    public Calendar strToCalendar(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Date 转化String
     *
     * @param date
     * @return
     */
    public String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * String 转化Date
     *
     * @param str "2018-2-2"
     * @return
     * @throws ParseException
     */
    public Date stringToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(str);
        return date;
    }

    /**
     * Date 转化Calendar
     *
     * @param calendar
     * @return
     */
    public Calendar dateToCalendar(Date date, Calendar calendar) {
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Calendar转化Date
     *
     * @param calendar
     * @return
     */
    public Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }


}
