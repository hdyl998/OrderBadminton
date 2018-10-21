package com.order.badminton;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.vise.xsnow.cache.SpCache;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.core.ApiCookie;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/4/14.
 */

public class App extends Application {
    UserInfoItem infoItem;
    private static App app;


    public HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();

        headers.put("X-Requested-With", "com.tencent.mm");
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043807 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Content-Type", "application/json;charset=UTF-8");



//        POST /v2/deal/save.do HTTP/1.1
//        Host	lgtyzx.ydmap.com.cn
//        Content-Length	329
//        Accept	application/json, text/plain, */*
//Origin	http://lgtyzx.ydmap.com.cn
//User-Agent	Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044033 Mobile Safari/537.36 MicroMessenger/6.6.6.1300(0x26060637) NetType/WIFI Language/zh_CN
//Content-Type	application/json;charset=UTF-8
//Referer	http://lgtyzx.ydmap.com.cn/booking/service/101359
//Accept-Encoding	gzip, deflate
//Accept-Language	zh-CN,en-US;q=0.8
//Cookie	wechat_user_openidwxab0d22963d2598ac=oLAbKjnnkwOleatdPwyCAHyi9Kgo; public_account=cwbnXAa4HxSuKxK1KhK2HwulbVLjaijiHxmlLR.yKh.wKxGlJAb1XULmWVPHXAa4H03zZEulJAbjZUDnZAa4HxCzKxCuMBawKxevOBCxMQ3hZ0ylJAbrZ0HnZESlMgavLRKvKBe1KhK2KQaqH1HjWUvMWUzjHxml3WgW3Xs73JgaHwulW07raEDscSjiHxmlKR.uMB.zHwulW07raEDscS3fZUSlMgdntnljqndjhJpitXNmepJisI1jt2KldO
//X-Requested-With	com.tencent.mm
        return headers;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ViseHttp.init(this);
        HashMap<String, String> params = new HashMap<>();


        ViseHttp.CONFIG()
                //配置全局请求头
                .globalHeaders(getHeaders())
                //配置全局请求参数
                .globalParams(params)
                //配置读取超时时间，单位秒
                .readTimeout(30)
                //配置写入超时时间，单位秒
                .writeTimeout(30)
                //配置连接超时时间，单位秒
                .connectTimeout(30)
                //配置请求失败重试次数
                .retryCount(3)
                //配置请求失败重试间隔时间，单位毫秒
                .retryDelayMillis(1000)
                //配置是否使用cookie
                .setCookie(true)
                //配置自定义cookie
                .apiCookie(new ApiCookie(this));

        initDatas();
    }

    private void initDatas() {
        SpCache spCache = new SpCache(App.getApp(), "logininfo");
        String ss = spCache.get("userinfo", null);
        if (ss != null) {
            try {
                infoItem = JSON.parseObject(ss, UserInfoItem.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static App getApp() {
        return app;
    }
}
