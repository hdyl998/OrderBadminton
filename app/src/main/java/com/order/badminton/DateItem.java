package com.order.badminton;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DateItem {
    public long day;
    public String dayName;
    public String weekName;


    public String getTitle() {
        return this.dayName + "\n" + this.weekName;
    }
//    		"day": 1523808000000,
//                    "dayName": "2018-04-16",
//                    "weekName": "星期一"

}
