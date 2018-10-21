package com.order.badminton;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.order.badminton.about.AboutActivity;
import com.order.badminton.makeorder.FinalOrderPostItem;
import com.order.badminton.makeorder.OrderParseItem;
import com.order.badminton.makeorder.OrderPostItem;
import com.order.badminton.setting.SettingActivity;
import com.order.badminton.setting.SettingConfig;
import com.order.badminton.view.PagerSlidingTabStrip;
import com.order.badminton.webview.WebViewActivity;
import com.vise.xsnow.cache.SpCache;
import com.vise.xsnow.http.ViseHttp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    PagerSlidingTabStrip stripDate, stripChang, stripType;
    TextView tvTitle;

    OrderMapView orderMapView;

    TextView tvName, tvOpenInfo, tvPrice, tvAllTime, tvChangInfo;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_tool_bar, menu);
//        return true;
//    }

    private boolean isOverTime;
    SettingConfig config = SettingConfig.getInstance();
    SpCache spCache = new SpCache(App.getApp(), "config");


    View rootView;
    View loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_launcher_badminton);//设置导航栏图标
        toolbar.setLogo(null);//设置app logo
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        toolbar.setTitle("羽毛球场地预订");//设置主标题
        toolbar.inflateMenu(R.menu.activity_tool_bar);//设置右上角的填充菜单
        //点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_setting://设置
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                    case R.id.action_login:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case R.id.action_orders:
                        goMyOrder();
                        break;
                    case R.id.action_home:
                        WebViewActivity.launch(MainActivity.this, "http://lgtyzx.ydmap.com.cn/page.shtml?id=101266");
                        break;
                    case R.id.action_usercenter:
                        WebViewActivity.launch(MainActivity.this, "http://lgtyzx.ydmap.com.cn/user/my");
                        break;
                    case R.id.action_pre_order:
                        WebViewActivity.launch(MainActivity.this, "http://lgtyzx.ydmap.com.cn/page.shtml?id=101297&dataType=this&dataId=102726");
                        break;
                }
                return true;
            }
        });

        rootView = findViewById(R.id.ll_content);
        loadingView = findViewById(R.id.progressBar);

        tvChangInfo = findViewById(R.id.tvChangInfo);
        stripDate = findViewById(R.id.menuDate);
        stripChang = findViewById(R.id.menuChang);
        tvTitle = findViewById(R.id.title);

        orderMapView = findViewById(R.id.orderMapView);
        tvName = findViewById(R.id.tvWelcome);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        tvOpenInfo = findViewById(R.id.tvOpenInfo);

        tvPrice = findViewById(R.id.tvPrice);
        tvAllTime = findViewById(R.id.tvAllTime);


        stripType = findViewById(R.id.menuType);

        int changmode = spCache.getInt("changmode", 0);
        stripType.initButtons(new String[]{"免费场", "收费场"}, changmode);
        salesId = salesIds[changmode];
        stripType.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                salesId = salesIds[position];
                spCache.putInt("changmode", position);
                getChangCiDate();
            }
        });


        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChangCiDate();
            }
        });

        //智能预定
        findViewById(R.id.btnPreOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAutoMode = false;
                doOrder();
            }
        });

        initViews();
        getDateListData();
        orderMapView.setInfoCallBacks(new OrderMapView.ICallBacks<String>() {
            @Override
            public void call(String s) {
                tvChangInfo.setText(s);
            }
        });
        orderMapView.setCallBacks(new OrderMapView.ICallBacks<int[][]>() {
            @Override
            public void call(int[][] maps) {
                List<ChangciBean.TimeSlotList> lists = changciBean.getTimeSlotList();

                int price = 0;
                int allTime = 0;

                for (int i = 0; i < maps.length; i++) {
                    for (int j = 0; j < maps[0].length; j++) {
                        if (maps[i][j] == 2) {//选中
                            ChangciBean.TimeSlotList timeSlotList = lists.get(i);//时间段
                            price += changciBean.getPrice(j, i);
                            allTime += timeSlotList.getTimeDuring();
                        }
                    }
                }
                tvPrice.setText(String.format("总价%.2f元", price / 100f));
                if (allTime > changciBean.getMaxBookTime()) {
                    isOverTime = true;
                    tvChangInfo.setText(changciBean.getMaxBookTimeDescr());
                } else {
                    isOverTime = false;
                }
                allTime /= 1000;

                int maxTime = (int) (changciBean.getMaxBookTime() / 1000);
                tvAllTime.setText(String.format("已订%d时%d分（最大可订%d时%d分）", allTime / 3600, allTime / 60 % 60
                        , maxTime / 3600, maxTime / 60 % 60
                ));
            }
        });
    }

    /***
     * 去我的订单
     */
    private void goMyOrder() {
        WebViewActivity.launch(MainActivity.this, "http://lgtyzx.ydmap.com.cn/order");
    }


    private int repeateCount;


    private void doOrder() {
        repeateCount = config.repeatTime;
        preOrder();
    }

    /***
     * 预订
     */
    private void preOrder() {
        repeateCount--;
        orderMapView.clearSelections();
        //算法
        int map[][] = orderMapView.getMaps();
        boolean isOK = SmartMakeOrder.doCalc(map, changciBean);

        if (isOK) {
            orderMapView.invalidate();
            //下单
            btnOK();
        }
    }


    private void initViews() {
        stripDate.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dateIndex = position;
                getChangCiDate();
            }
        });

        //下一步
        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAutoMode = false;
                btnOK();
            }
        });
    }

    private void btnOK() {
        if (isOverTime) {
            new AlertDialog.Builder(MainActivity.this).setMessage(changciBean.getMaxBookTimeDescr())
                    .setPositiveButton("OK", null).create().show();
        } else {//下一步下单
            List<OrderPostItem> lists = changciBean.bulidOrderPostItem(getOrderDate(), changciBean.getSingleMinBookTime(), orderMapView.getMaps());
            if (lists == null) {
                ToastUtils.makeTextAndShow(String.format("单个场地至少需要预订%d分钟", changciBean.getSingleMinBookTime() / 1000 / 60));
                return;
            }
            makeOrder(lists);
        }
    }


    //下订单
    private void makeOrder(final List<OrderPostItem> lists) {
//http://lgtyzx.ydmap.com.cn/v2/sportPlatformUser/queryByDealPlatform.do
        JSONObject object = new JSONObject();
        object.put("dealPlatformList", lists);
//        System.out.println(JSON.toJSONString(object));
        ViseHttp.POST("queryByDealPlatform.do")
                .baseUrl("http://lgtyzx.ydmap.com.cn/v2/sportPlatformUser/")
                .setJson(JSON.toJSONString(object)).request(new MyActionCallback<String>() {

            @Override
            public void onSuccessFinal(NetEntity data) {
                OrderParseItem parseItem = JSON.parseObject(data.data, OrderParseItem.class);
                handinOrder(parseItem, lists);
            }

        });
    }

    //提交订单
    private void handinOrder(final OrderParseItem parseItem, final List<OrderPostItem> lists) {
        //自动下单,不需要二次提确认提示
        if (config.isDoubleCheck && !isAutoMode) {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage(parseItem.getPlatformListInfo())
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认无误，立即下单", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            makeOrderFinal(parseItem, lists);
                        }
                    }).create().show();
        } else {
            makeOrderFinal(parseItem, lists);
        }
    }

    /***
     * 立即下单
     * @param parseItem
     * @param lists
     */
    private void makeOrderFinal(OrderParseItem parseItem, List<OrderPostItem> lists) {

        List<FinalOrderPostItem> temps = new ArrayList<>(lists.size());
        for (OrderPostItem item : lists) {
            FinalOrderPostItem postItem = new FinalOrderPostItem(item);
            postItem.fightMobile = parseItem.publicAccount.mobile;
            if (parseItem.sportTeamList.size() > 0) {
                postItem.sportTeamId = parseItem.sportTeamList.get(0).sportTeamId;
            }
            temps.add(postItem);
        }

        JSONObject object = new JSONObject();
        object.put("dealServiceUserList", new ArrayList<>());
        object.put("dealPlatformList", temps);

        System.out.println(JSON.toJSONString(object));


        ViseHttp.POST("save.do")
                .baseUrl("http://lgtyzx.ydmap.com.cn/v2/deal/")
                .setJson(JSON.toJSONString(object)).request(new MyActionCallback<String>() {

            @Override
            public void onSuccessFinal(NetEntity data) {
                JSONObject obj = JSON.parseObject(data.data);
                String dealId = obj.getString("dealId");
                if (dealId != null) {
                    mDealId = dealId;
                    //刷新列表
                    getOrderInfo(new OrderMapView.ICallBacks() {
                        @Override
                        public void call(Object o) {
                            if (repeateCount != 0) {
                                preOrder();
                            }
                        }
                    });
                    successOrderMessage();//显示成功消息
                } else {
                    ToastUtils.makeTextAndShow("下单失败");
                }
            }
        });
    }

    private void go2OrderDetail() {
        WebViewActivity.launch(MainActivity.this, "http://lgtyzx.ydmap.com.cn/order/" + mDealId);
    }

    //订单ID
    private String mDealId;

    AlertDialog dialog;

    private void successOrderMessage() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("下单成功！快到微信里面付款吧！")
                    .setCancelable(false)
                    .setPositiveButton("完成", null).create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    stopMusic();
                    go2OrderDetail();

                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
            playMusic();
        }
    }

    MediaPlayer player;

    private void playMusic() {
        if (!config.isSound) {
            return;
        }
        try {
            player = MediaPlayer.create(this, R.raw.super_mario_bgm);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        try {
            if (player != null) {
                player.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
    }


    private int[] salesIds = {101359, 101395};//免费收费

    private int salesId = 101359;//免费的
    private int itemId = 200002;//羽毛球


    List<DateItem> listDates;

    int dateIndex = -1;

    private void getDateListData() {
        ViseHttp.GET(String.format("queryCalendarList.do?salesId=%d&itemId=%d&t=%d", salesId, itemId,
                DateUtils.getCurrentMills()))
                .baseUrl("http://lgtyzx.ydmap.com.cn/v2/sportPlatform/")
                .request(new MyActionCallback<String>() {
                    @Override
                    public void onSuccessFinal(NetEntity data) {
                        loadingView.setVisibility(View.GONE);
                        rootView.setVisibility(View.VISIBLE);

                        listDates = JSON.parseArray(data.data, DateItem.class);
                        for (int i = 0; i < 100; i++) {
                            DateItem newItem = new DateItem();
                            newItem.day = listDates.get(listDates.size() - 1).day + 3600 * 1000 * 24;
                            newItem.dayName = DateUtils.getDateString(newItem.day);
                            newItem.weekName = DateUtils.getWeekOfDate(newItem.day);
                            listDates.add(newItem);
                        }
                        String arrs[] = new String[listDates.size()];
                        int count = 0;
                        for (DateItem item : listDates) {
                            arrs[count++] = item.getTitle();
                        }
                        if (dateIndex == -1) {
                            dateIndex = 2;
                        }
                        stripDate.initButtons(arrs, dateIndex);
                        getChangCiDate();
                    }
                });
    }

    private long getOrderDateLong() {
        return listDates.get(dateIndex).day;
    }

    private String getOrderDate() {
        return listDates.get(dateIndex).dayName;
    }

    ChangciBean changciBean;

    private void getChangCiDate() {
        ViseHttp.GET(String.format("querySportPlatformInfo.do?salesId=%d&itemId=%d&curDate=%d&t=%d", salesId, itemId,
                getOrderDateLong(),
                DateUtils.getCurrentMills()))
                .baseUrl("http://lgtyzx.ydmap.com.cn/v2/sportPlatform/")
                .request(new MyActionCallback<String>() {
                    @Override
                    public void onSuccessFinal(NetEntity data) {
                        changciBean = JSON.parseObject(data.data, ChangciBean.class);
                        String arrs[] = new String[changciBean.getSportPlatformList().size()];

                        for (ChangciBean.SportPlatformList item : changciBean.getSportPlatformList()) {
                            if (item.getPlatformName().startsWith("羽毛球")) {
                                item.simplePlatformName = item.getPlatformName().substring(3);
                                item.isVip = false;
                            } else {
                                item.simplePlatformName = item.getPlatformName();
                                item.isVip = true;
                            }
                        }
                        int count = 0;
                        for (ChangciBean.SportPlatformList item : changciBean.getSportPlatformList()) {
                            arrs[count++] = item.simplePlatformName;
                        }
                        stripChang.initButtons(arrs);
                        tvTitle.setText(changciBean.getSalesName());
                        DateUtils.getInstance().setCurrentServerTime(changciBean.getCurTime());
                        orderMapView.setBean(changciBean);
                        getOrderInfo(null);
                        handler.removeCallbacksAndMessages(null);
                        updateChaTime();
                        updateAutoRun();
                    }
                });
    }

    private void updateChaTime() {
        if (changciBean.getBookStartTime() != 0) {
            long cha = changciBean.getBookStartTime() - DateUtils.getCurrentMills();
            if (cha > 0) {
                tvOpenInfo.setText(DateUtils.getReadAbleTimeString(cha) + " 后开售");
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                handler.removeMessages(0);
            }
        } else {
            handler.removeMessages(0);
            tvOpenInfo.setText(null);
        }
    }


    private void updateAutoRun() {
        handler.removeCallbacks(orderRunable);
        if (changciBean.getBookStartTime() != 0) {
            long cha = changciBean.getBookStartTime() - DateUtils.getCurrentMills();
            ToastUtils.makeTextAndShow(String.format("%s 后开始下单!", DateUtils.getReadAbleTimeString(cha)));
            handler.postDelayed(orderRunable, cha);
        }
    }


    private boolean isAutoMode = false;

    private Runnable orderRunable = new Runnable() {
        @Override
        public void run() {
            ToastUtils.makeTextAndShow("开始自动预定中...");
            tvOpenInfo.setText("开始自动预定中...!!!");
            isAutoMode = true;
            doOrder();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateChaTime();
        }
    };


    private void getOrderInfo(final OrderMapView.ICallBacks callBacks) {
        ViseHttp.GET(String.format("queryDealMiniInfo.do?salesId=%d&itemId=%d&curDate=%d&t=%d", salesId, itemId,
                getOrderDateLong(),
                DateUtils.getCurrentMills()))
                .baseUrl("http://lgtyzx.ydmap.com.cn/v2/deal/")
                .request(new MyActionCallback<String>() {
                    @Override
                    public void onSuccessFinal(NetEntity data) {
                        List<OrderItem> orderItems = JSON.parseArray(data.data, OrderItem.class);
                        orderMapView.setOrderItemList(orderItems);
                        if (callBacks != null)
                            callBacks.call(null);
                    }
                });
    }

    private void updateUserInfo() {
        if (App.getApp().infoItem != null)
            tvName.setText(String.format("登录信息：%s", App.getApp().infoItem.toString()));
    }


}
