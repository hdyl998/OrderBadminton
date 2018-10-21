package com.order.badminton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class OrderMapView extends View {


    public OrderMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    Paint mPaint;


    public float textHeight;


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(dip2px(getContext(), 8.5f));
        textHeight = (mPaint.descent() - mPaint.ascent());

//        fontHeight = (int) (mPaint.getFontMetrics().top - mPaint.getFontMetrics().bottom);
    }


    OrderItem[][] orderItems;


    public List<ChangciBean.TimeSlotList> listYs;
    public List<ChangciBean.SportPlatformList> listXs;


    ChangciBean bean;


    public void setBean(ChangciBean bean) {
        this.bean = bean;
        this.listXs = bean.getSportPlatformList();
        this.listYs = bean.getTimeSlotList();
        orderItems = null;
        maps = null;
        requestLayout();
    }

    private int getXIndex(OrderItem item) {
        int index = 0;
        for (ChangciBean.SportPlatformList temp : listXs) {
            if (item.platformId == temp.platformId) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private int[] getYIndex(OrderItem item) {
        int index = 0;

        int startIndex = 0;
        for (ChangciBean.TimeSlotList temp : listYs) {
            if (item.startTime == temp.startTime) {
                if (item.endTime == temp.endTime)
                    return new int[]{index, index};
                else {
                    startIndex = index;
                }
            }
            if (item.endTime == temp.endTime) {
                return new int[]{startIndex, index};
            }
            index++;
        }
        return null;
    }


    int maps[][];

    public int[][] getMaps() {
        return maps;
    }

    public void clearSelections() {
        boolean isIn = false;
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[0].length; j++) {
                if (maps[i][j] == 2) {
                    maps[i][j] = 0;
                    isIn = true;
                }
            }
        }
        if (isIn) {
            invalidate();
        }
    }


    public void setOrderItems(OrderItem[][] orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        orderItems = new OrderItem[listYs.size()][listXs.size()];

        maps = new int[listYs.size()][listXs.size()];
        for (OrderItem item : orderItemList) {
            //取消
            if (item.dealState == 3) {
                continue;
            }

            int x = getXIndex(item);
            int ys[] = getYIndex(item);
            for (int s = ys[0]; s <= ys[1]; s++) {
                orderItems[s][x] = item;
                maps[s][x] = 1;
            }
        }

        invalidate();
    }

    int oneX;
    int oneY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0) {
            return;
        }

        if (orderItems == null) {
            return;
        }

        int xCount = listXs.size();
        int yCount = listYs.size();


        oneX = getWidth() / xCount;

        oneY = getHeight() / yCount;


        for (int i = 0; i < yCount; i++) {
            for (int j = 0; j < xCount; j++) {
                int var = maps[i][j];
                switch (var) {
                    case 0://没有选中
                        break;
                    case 1:
                        mPaint.setColor(0xff787878);
                        canvas.drawRect(oneX * j, oneY * i, oneX * j + oneX, oneY * i + oneY, mPaint);
                        break;
                    case 2://表示成选中
                        mPaint.setColor(0xffDD4A4A);
                        canvas.drawRect(oneX * j, oneY * i, oneX * j + oneX, oneY * i + oneY, mPaint);
                        break;
                }
            }
        }
        mPaint.setColor(Color.GRAY);
        for (int i = 0; i < xCount; i++) {
            canvas.drawLine(oneX * i, 0, oneX * i, getHeight(), mPaint);
        }
        for (int j = 0; j < yCount; j++) {
            canvas.drawLine(0, oneY * j, getWidth(), oneY * j, mPaint);
        }
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.BLUE);
        for (int i = 0; i < yCount; i++) {
            for (int j = 0; j < xCount; j++) {
                OrderItem item = orderItems[i][j];
                if (item != null) {
                    String temp = item.pubRealName;
                    if (temp.length() > 3) {
                        temp = temp.substring(0, 3);
                    }
                    canvas.drawText(temp, oneX * j + oneX / 2, oneY * i + oneY / 2 + textHeight / 2, mPaint);
                }
            }
        }
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < yCount; i++) {
            ChangciBean.TimeSlotList slotList = listYs.get(i);
            canvas.drawText(slotList.getStartTimeValue(), 5, oneY * i + oneY / 4 + textHeight / 2, mPaint);
            canvas.drawText(slotList.getEndTimeValue(), 5, oneY * i + oneY * 3 / 4 + textHeight / 2, mPaint);
        }
        if (iCallBacks != null)
            iCallBacks.call(maps);
    }


    public interface ICallBacks<T> {
        public void call(T t);
    }

    ICallBacks iCallBacks, infoCallBacks;


    public void setCallBacks(ICallBacks iCallBacks) {
        this.iCallBacks = iCallBacks;
    }

    public void setInfoCallBacks(ICallBacks<String> infoCallBacks) {
        this.infoCallBacks = infoCallBacks;
    }

    float x;
    float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listXs == null) {
            return true;
        }
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float x1 = event.getX();
                    float y1 = event.getY();
                    if (Math.abs(x1 - x) < 10 && Math.abs(y1 - y) < 10) {
                        int xx = (int) (x1 / oneX);
                        int yy = (int) (y1 / oneY);
                        int value = maps[yy][xx];
                        if (value == 1) {
                            if (infoCallBacks != null)
                                infoCallBacks.call(orderItems[yy][xx].toString());
                        } else {
                            if (value == 0) {
                                maps[yy][xx] = 2;
                                ChangciBean.SportPlatformList ddd = listXs.get(xx);
                                ChangciBean.TimeSlotList slotList = listYs.get(yy);
                                int price = bean.getPrice(xx, yy);
                                price /= 100;
                                if (infoCallBacks != null)
                                    infoCallBacks.call(String.format("%s %s %s", price == 0 ? "免费" : price + "元", ddd.getPlatformName(),
                                            slotList.getTimeString()));
                                invalidate();
                            } else {
                                maps[yy][xx] = 0;
                                if (infoCallBacks != null)
                                    infoCallBacks.call(null);
                                invalidate();
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (listYs == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        setMeasuredDimension(widthMeasureSpec, dip2px(getContext(), listYs.size() * 30));
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
