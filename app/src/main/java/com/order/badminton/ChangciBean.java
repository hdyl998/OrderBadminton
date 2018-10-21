package com.order.badminton;

import com.order.badminton.makeorder.OrderPostItem;
import com.order.badminton.setting.SettingConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class ChangciBean {

    public int getPrice(int x, int y) {
        TimeSlotList timeSlotList = getYData(y);
        SportPlatformList platformList = getXData(x);
        for (SportPlatformPriceList priceList : getSportPlatformPriceList()) {
            if (priceList.priceTagId == platformList.platformPriceId && priceList.startTime <= timeSlotList.startTime && priceList.endTime >= timeSlotList.endTime) {
                return (int) priceList.price;
            }
        }
        return 0;
    }

    public int getYStartIndex(String time) {
        int index = 0;
        for (TimeSlotList slot : getTimeSlotList()) {
            if (slot.getStartTimeValue().compareTo(time) >= 0) {
                return index;
            }
            index++;
        }
        return index;
    }

    public int getYEndIndex(String time) {

        for (int i = getTimeSlotList().size() - 1; i >= 0; i--) {
            if (getTimeSlotList().get(i).getEndTimeValue().compareTo(time) <= 0) {
                return i;
            }
        }
        return -1;
    }

    /***
     * 是否时间跨度超出最大选择时间
     * @param startYIndex
     * @param endYIndex
     * @return
     */
    public boolean isRangeTimeOverMaxTime(int startYIndex, int endYIndex) {
        long cha = getTimeSlotList().get(endYIndex).getEndTime()
                - getTimeSlotList().get(startYIndex).getStartTime();
        return cha > getMaxBookTime();
    }

    public int getMaxOrderYCount() {
        return (int) (getMaxBookTime() / getTimeSlotList().get(0).getTimeDuring());
    }


    private int getXIndex(String preSelect) {
        int index = 0;
        for (SportPlatformList list : getSportPlatformList()) {
            if (preSelect.equals(list.getSimplePlatformName())) {
                return index;
            }
            index++;
        }
        return -1;
    }


    public List<Integer> getXIndexs(SettingConfig config) {

        int len = getSportPlatformList().size();
        List<Integer> lists = new ArrayList<>(len);
        //添加优先选择的场地
        for (String strNum : config.firstChang) {
            int index = getXIndex(strNum);
            if (index != -1)
                lists.add(index);
        }
        for (int i = 0; i < len; i++) {
            //不包含
            if (!lists.contains(i)) {
                boolean isVip = getSportPlatformList().get(i).isVip;
                if (isVip && !config.isOrderVipFlag) {
                    continue;
                } else {
                    lists.add(i);
                }
            }
        }

        return lists;
    }

    public TimeSlotList getYData(int y) {
        return getTimeSlotList().get(y);
    }

    public SportPlatformList getXData(int x) {
        return getSportPlatformList().get(x);
    }

    /***
     * 创建下订单的数据
     * @param singleMinBookTime
     * @param maps
     * @return
     */
    public List<OrderPostItem> bulidOrderPostItem(String orderDate, long singleMinBookTime, int maps[][]) {
        List<OrderPostItem> lists = new ArrayList<>();
        for (int x = 0; x < maps[0].length; x++) {

            OrderPostItem item = null;
            for (int y = 0; y < maps.length; y++) {
                {
                    if (maps[y][x] == 2) {//选中
                        if (item == null) {
                            item = new OrderPostItem();
                            lists.add(item);
                            item.setStartTime(getYData(y).startTime);
                            item.setColIndex(x);
                            item.setRowIndex(y);
                            item.setOrderDate(orderDate);
                            item.setPlatformId(getXData(x).getPlatformId());
                            item.setPlatformParentId(getXData(x).getParentId());
                            item.setSalesId(salesId + "");
                        }
                        item.setEndTime(getYData(y).endTime);
                        item.rowspan++;//自增
                    } else {
                        item = null;
                    }
                }
            }
        }
        if (lists.size() == 0) {
            return null;
        }

        for (OrderPostItem item : lists) {
            item.setRowIndex(item.getRowIndex() + item.getRowspan() - 1);
            if (item.getTimeDuring() < singleMinBookTime) {
                return null;
            }
        }
        return lists;
    }


    public String bookAlert;

    public long bookStartTime;

    public long curTime;

    public long everyAddTime;

    public long isViewPrice;

    public long itemId;

    public long maxBookTime;

    public String maxBookTimeDescr;

    public long maxBuyNum;

    public long salesId;

    public String salesName;

    public long singleMinBookTime;

    public List<SportPlatformList> sportPlatformList;

    public List<SportPlatformPriceList> sportPlatformPriceList;

    public List<TimeSlotList> timeSlotList;

    public void setBookAlert(String bookAlert) {
        this.bookAlert = bookAlert;
    }

    public String getBookAlert() {
        return this.bookAlert;
    }

    public void setBookStartTime(long bookStartTime) {
        this.bookStartTime = bookStartTime;
    }

    public long getBookStartTime() {
        return this.bookStartTime;
    }

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }

    public long getCurTime() {
        return this.curTime;
    }

    public void setEveryAddTime(long everyAddTime) {
        this.everyAddTime = everyAddTime;
    }

    public long getEveryAddTime() {
        return this.everyAddTime;
    }

    public void setIsViewPrice(long isViewPrice) {
        this.isViewPrice = isViewPrice;
    }

    public long getIsViewPrice() {
        return this.isViewPrice;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setMaxBookTime(long maxBookTime) {
        this.maxBookTime = maxBookTime;
    }

    public long getMaxBookTime() {
        return this.maxBookTime;
    }

    public void setMaxBookTimeDescr(String maxBookTimeDescr) {
        this.maxBookTimeDescr = maxBookTimeDescr;
    }

    public String getMaxBookTimeDescr() {
        return this.maxBookTimeDescr;
    }

    public void setMaxBuyNum(long maxBuyNum) {
        this.maxBuyNum = maxBuyNum;
    }

    public long getMaxBuyNum() {
        return this.maxBuyNum;
    }

    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    public long getSalesId() {
        return this.salesId;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSalesName() {
        return this.salesName;
    }

    public void setSingleMinBookTime(long singleMinBookTime) {
        this.singleMinBookTime = singleMinBookTime;
    }

    public long getSingleMinBookTime() {
        return this.singleMinBookTime;
    }

    public void setSportPlatformList(List<SportPlatformList> sportPlatformList) {
        this.sportPlatformList = sportPlatformList;
    }

    public List<SportPlatformList> getSportPlatformList() {
        return this.sportPlatformList;
    }

    public void setSportPlatformPriceList(List<SportPlatformPriceList> sportPlatformPriceList) {
        this.sportPlatformPriceList = sportPlatformPriceList;
    }

    public List<SportPlatformPriceList> getSportPlatformPriceList() {
        return this.sportPlatformPriceList;
    }

    public void setTimeSlotList(List<TimeSlotList> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public List<TimeSlotList> getTimeSlotList() {
        return this.timeSlotList;
    }

    public static class SportPlatformList {
        public long onlineBooking;


        public long parentId;

        public long platformId;

        public String platformName;

        public String simplePlatformName;

        public boolean isVip;


        public void setSimplePlatformName(String simplePlatformName) {
            this.simplePlatformName = simplePlatformName;
        }

        public String getSimplePlatformName() {
            return simplePlatformName;
        }

        public long platformPriceId;

        public void setOnlineBooking(long onlineBooking) {
            this.onlineBooking = onlineBooking;
        }

        public long getOnlineBooking() {
            return this.onlineBooking;
        }

        public void setParentId(long parentId) {
            this.parentId = parentId;
        }

        public long getParentId() {
            return this.parentId;
        }

        public void setPlatformId(long platformId) {
            this.platformId = platformId;
        }

        public long getPlatformId() {
            return this.platformId;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPlatformName() {
            return this.platformName;
        }

        public void setPlatformPriceId(long platformPriceId) {
            this.platformPriceId = platformPriceId;
        }

        public long getPlatformPriceId() {
            return this.platformPriceId;
        }
    }


    public static class SportPlatformPriceList {
        public long endTime;

        public long fee;

        public long price;

        public long priceTagId;

        public long startTime;

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setFee(long fee) {
            this.fee = fee;
        }

        public long getFee() {
            return this.fee;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public long getPrice() {
            return this.price;
        }

        public void setPriceTagId(long priceTagId) {
            this.priceTagId = priceTagId;
        }

        public long getPriceTagId() {
            return this.priceTagId;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getStartTime() {
            return this.startTime;
        }
    }

    public static class TimeSlotList {

        public String getTimeString() {
            return String.format("%s-%s", startTimeValue, endTimeValue);
        }

        public long endTime;

        public String endTimeValue;

        public String fee;

        public String feeValue;

        public String longerVal;

        public long price;

        public String priceValue;

        public long startTime;


        public long getTimeDuring() {
            return endTime - startTime;
        }


        public String startTimeValue;

        public long viewType;

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTimeValue(String endTimeValue) {
            this.endTimeValue = endTimeValue;
        }

        public String getEndTimeValue() {
            return this.endTimeValue;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getFee() {
            return this.fee;
        }

        public void setFeeValue(String feeValue) {
            this.feeValue = feeValue;
        }

        public String getFeeValue() {
            return this.feeValue;
        }

        public void setlongerVal(String longerVal) {
            this.longerVal = longerVal;
        }

        public String getlongerVal() {
            return this.longerVal;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public long getPrice() {
            return this.price;
        }

        public void setPriceValue(String priceValue) {
            this.priceValue = priceValue;
        }

        public String getPriceValue() {
            return this.priceValue;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTimeValue(String startTimeValue) {
            this.startTimeValue = startTimeValue;
        }

        public String getStartTimeValue() {
            return this.startTimeValue;
        }

        public void setViewType(long viewType) {
            this.viewType = viewType;
        }

        public long getViewType() {
            return this.viewType;
        }
    }


}
