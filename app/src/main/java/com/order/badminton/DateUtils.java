package com.order.badminton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DateUtils {

    final static DateUtils utils = new DateUtils();

    public static DateUtils getInstance() {
        return utils;
    }

    public long cha = 0;


    public static long getCurrentMills() {
        return System.currentTimeMillis() + getInstance().cha;
    }


    public void setCurrentServerTime(long serverTime) {
        this.cha = serverTime - System.currentTimeMillis();
    }

    public static long getCurrentDate() {
        long currentTime = getCurrentMills();

        Date date = new Date(currentTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = dateFormat.format(date);
        try {
            return dateFormat.parse(newDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getCurDateString() {
        long currentTime = getCurrentMills();
        return getDateString(currentTime);
    }

    public static String getDateString(long dateLong) {
        Date date = new Date(dateLong);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param time
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(long time) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public static String getReadAbleTimeString(long time) {
        int allSeconds = (int) (time / 1000);
        int hour = allSeconds / 3600;
        int minute = allSeconds / 60 % 60;
        int seconds = allSeconds % 60;
        return String.format("%d时%d分%d秒", hour, minute, seconds);
    }

}
