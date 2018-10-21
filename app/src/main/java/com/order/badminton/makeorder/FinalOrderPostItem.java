package com.order.badminton.makeorder;

/**
 * Created by Administrator on 2018/4/15.
 */

public class FinalOrderPostItem extends OrderPostItem {

    public int sportTeamId;//ID
    public String sportTeamColor = null;
    public String fightDeclaration = null;
    public String fightMobile;//电话


    public FinalOrderPostItem(OrderPostItem item) {
        salesId = item.salesId;
        platformParentId = item.platformParentId;
        platformId = item.platformId;
        orderDate = item.orderDate;
        startTime = item.startTime;
        endTime = item.endTime;
        colspan = item.colspan;
        rowspan = item.rowspan;
        colIndex = item.colIndex;
        rowIndex = item.rowIndex;
    }
}
