package com.order.badminton.makeorder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class OrderParseItem {
    public List<DealPlatformList> dealPlatformList;


    public String getPlatformListInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        for (DealPlatformList item : dealPlatformList) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(item.toString());
        }
        return stringBuilder.toString();
    }


    public PublicAccount publicAccount;

    public List<SportTeamList> sportTeamList;

    public static class PublicAccount {
        public int companyId;

        public String companyName;

        public String mobile;

        public int pubAccountId;

        public int pubUserId;

        public String realName;


    }

    public static class DealPlatformList {

        @Override
        public String toString() {
            return String.format("场地：%s(%s %s-%s) 共%s小时 %.2f元", platformName, orderDateValue,
                    startTimeValue, endTimeValue, bookingTime, platformPrice / 100f
            );
        }

        public double bookingTime;

        public String dealPlatformId;

        public long endTime;

        public String endTimeValue;

        public String fightDeclaration;

        public String fightMobile;

        public int isFight;

        public long orderDate;

        public String orderDateValue;

        public int platformId;

        public String platformName;

        public int platformParentId;

        public String platformParentName;

        public int platformPrice;

        public int professionalId;

        public int salesId;

//        public List<SportPlatformUserList> sportPlatformUserList;

        public String sportTeamColor;

        public String sportTeamId;

        public long startTime;

        public String startTimeValue;


    }

    public static class SportTeamList {
        public int professionalId;

        public String sportName;

        public int sportTeamId;


    }


}
