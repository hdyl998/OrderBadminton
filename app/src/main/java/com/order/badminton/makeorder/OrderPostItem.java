package com.order.badminton.makeorder;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2018/4/15.
 */

public class OrderPostItem {
    public String salesId;

    public long platformParentId;

    public long platformId;

    public String orderDate;

    public long startTime;

    public long endTime;

    public int colspan = 1;

    public int rowspan;

    public int colIndex;

    public int rowIndex;

    @JSONField(serialize = false)
    public long getTimeDuring() {
        return endTime - startTime;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getSalesId() {
        return this.salesId;
    }

    public void setPlatformParentId(long platformParentId) {
        this.platformParentId = platformParentId;
    }

    public long getPlatformParentId() {
        return this.platformParentId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public long getPlatformId() {
        return this.platformId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getColspan() {
        return this.colspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public int getRowspan() {
        return this.rowspan;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public int getColIndex() {
        return this.colIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

}
