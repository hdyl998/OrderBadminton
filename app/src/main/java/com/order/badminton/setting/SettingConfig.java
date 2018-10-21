package com.order.badminton.setting;

import com.alibaba.fastjson.JSON;
import com.order.badminton.App;
import com.vise.xsnow.cache.SpCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class SettingConfig {

    private final static SettingConfig settingConfig;

    static {
        settingConfig = create();
    }

    public static SettingConfig getInstance() {
        return settingConfig;
    }

    private static SettingConfig create() {
        SpCache spCache = new SpCache(App.getApp(), "setting");
        String cache = spCache.get("config", null);
        try {
            if (cache != null) {
                SettingConfig settingConfig = JSON.parseObject(cache, SettingConfig.class);
                return settingConfig;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SettingConfig();
    }


    public void setDoubleCheck(boolean doubleCheck) {
        isDoubleCheck = doubleCheck;
    }

    public void setOrderVipFlag(boolean orderVipFlag) {
        isOrderVipFlag = orderVipFlag;
    }

    public boolean isDoubleCheck = true;
    public int maxOrderCount = 1;
    public String startTime = "19:00";
    public String endTime = "21:00";
    public String prefer = "时间优先";
    public int repeatTime = 1;
    public boolean isSound =true;


    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setPrefer(String prefer) {
        this.prefer = prefer;
    }


    public String getPrefer() {
        return prefer;
    }

    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }

    public boolean isOrderVipFlag = false;//是否能定VIP房间

    //首选场
    public List<String> firstChang = new ArrayList<>(3);


    private SettingConfig() {
        firstChang.add("13");
    }


    SpCache spCache = new SpCache(App.getApp(), "setting");

    public void save() {
        spCache.put("config", JSON.toJSONString(this));
    }

    public void setMaxOrderCount(int maxOrderCount) {
        this.maxOrderCount = maxOrderCount;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
