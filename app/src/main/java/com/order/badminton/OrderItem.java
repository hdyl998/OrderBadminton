package com.order.badminton;

/**
 * Created by Administrator on 2018/4/14.
 */

public class OrderItem {

    public String getTimeString() {
        return String.format("%s-%s", startTimeValue, endTimeValue);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", pubRealName, fightMobile, getTimeString());
    }

    public long dealId;

    public String dealItem;

    public long dealPlatformId;

    public String dealServiceUser;

    public String dealServiceUserId;

    public String dealServiceUserList;

    public String dealSign;

    public long dealState;

    public String dealUserStr;

    public String descr;

    public long endTime;

    public String endTimeValue;

    public String fightDeclaration;

    public String fightMobile;

    public long isFightDeal;

    public long isForever;

    public long orderDate;

    public long platformId;

    public String platformSubIds;

    public String price;

    public String priceValue;

    public long professionalId;

    public String pubMobile;

    public String pubRealName;

    public long salesId;

    public String sellerMessage;

    public String sportTeamColor;

    public String sportTeamColorValue;

    public String sportTeamId;

    public String sportTeamName;

    public long srvId;

    public long startTime;

    public String startTimeValue;

    public String userMessage;

}
