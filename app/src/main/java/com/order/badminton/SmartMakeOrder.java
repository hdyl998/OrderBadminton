package com.order.badminton;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.order.badminton.setting.SettingConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 智能计算需要
 * Created by Administrator on 2018/4/15.
 */

public class SmartMakeOrder {
    public static boolean doCalc(int arr[][], ChangciBean bean) {
        SettingConfig config = SettingConfig.getInstance();

        int minYIndex = bean.getYStartIndex(config.startTime);
        int maxYIndex = bean.getYEndIndex(config.endTime);
        System.out.println(minYIndex);
        System.out.println(maxYIndex);
        if (maxYIndex < minYIndex) {
            ToastUtils.makeTextAndShow(String.format("暂无可订场次[minYIndex=%d,maxYIndex=%d]!", minYIndex, maxYIndex));
            return false;
        }

        List<Integer> lists = bean.getXIndexs(config);
        System.out.println(JSON.toJSONString(lists));
        //时间范围太大，则每列只取最大时间，一次最多只能订最大场,计算得出数量
        boolean isOverTime = bean.isRangeTimeOverMaxTime(minYIndex, maxYIndex);
        int successNum = config.maxOrderCount;
        if (isOverTime == false) {
            for (Integer x : lists) {
                if (orderCol(arr, x, minYIndex, maxYIndex)) {
                    successNum--;
                    if (successNum == 0) {
                        break;
                    }
                }
            }
        } else {
            int maxOrderY = bean.getMaxOrderYCount();
            ToastUtils.makeTextAndShow(String.format("所选时间超出单次最大能定场数(%d场),采用[%s]算法预定中...", maxOrderY, config.prefer));
            if ("场次优先".equals(config.prefer)) {
                for (Integer x : lists) {
                    if (orderCol2(arr, x, minYIndex, maxYIndex, maxOrderY)) {
                        successNum--;
                        break;
                    }
                }
            } else {//时间优先
                List<XYInfo> listInfos = new ArrayList<>(lists.size());
                for (Integer x : lists) {
                    int y = getOrderYIndex(arr, x, minYIndex, maxYIndex, maxOrderY);
                    //寻找满足条件的y的起始值,最优解是y最小的
                    if (y != -1) {
                        listInfos.add(new XYInfo(x, y));
                    }
                }
                Collections.sort(listInfos);//排序,找y最小的
                System.out.println(JSON.toJSONString(listInfos));
                for (XYInfo info : listInfos) {
                    doOrderCol(arr, info.x, info.y, maxYIndex, maxOrderY);
                    successNum--;
                    break;
                }
            }
        }
        if (successNum == config.maxOrderCount) {
            ToastUtils.makeTextAndShow("暂无可订场次！");
            return false;
        }
        return true;
    }


    private static class XYInfo implements Comparable<XYInfo> {

        public XYInfo(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;

        @Override
        public int compareTo(@NonNull XYInfo o) {
            return y - o.y;
        }
    }

    /***
     * 对一列进行定制
     * @param arr
     * @param yStart
     * @param yEnd
     * @return
     */
    private static boolean orderCol(int arr[][], int xCol, int yStart, int yEnd) {
        for (int y = yStart; y <= yEnd; y++) {
            int var = arr[y][xCol];
            if (var != 0) {
                return false;
            }
        }
        //让选中
        for (int y = yStart; y <= yEnd; y++) {
            arr[y][xCol] = 2;
        }
        return true;
    }

    /***
     * 获得xCol列,最先符合条件的y值
     * @param arr
     * @param xCol
     * @param yStart
     * @param yEnd
     * @param maxOrderY
     * @return
     */
    private static int getOrderYIndex(int arr[][], int xCol, int yStart, int yEnd, int maxOrderY) {
        int count = 0;
        int endIndex = 0;
        for (int y = yStart; y <= yEnd; y++) {
            int var = arr[y][xCol];
            if (var == 0) {
                count++;
                if (count == maxOrderY) {
                    endIndex = y;
                    break;
                }
            } else {
                count = 0;
            }
        }
        if (count != maxOrderY) {
            return -1;
        }
        return endIndex - count + 1;
    }


    /***
     * 对一列进行定制
     * @param arr
     * @param yStart
     * @param yEnd
     * @return
     */
    private static boolean orderCol2(int arr[][], int xCol, int yStart, int yEnd, int maxOrderY) {
        int startYIndex = getOrderYIndex(arr, xCol, yStart, yEnd, maxOrderY);
        if (startYIndex == -1) {
            return false;
        }
        doOrderCol(arr, xCol, startYIndex, yEnd, maxOrderY);
        return true;
    }

    private static void doOrderCol(int arr[][], int xCol, int startYIndex, int yEnd, int maxOrderY) {
        //可以匹配到需要的场数
        //让选中
        for (int y = startYIndex; y <= yEnd; y++) {
            arr[y][xCol] = 2;
            maxOrderY--;
            if (maxOrderY == 0) {
                break;
            }
        }
    }
}
