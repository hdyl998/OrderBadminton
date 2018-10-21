package com.order.badminton.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.order.badminton.App;
import com.order.badminton.R;
import com.vise.xsnow.http.ViseHttp;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class WebViewActivity extends AppCompatActivity {

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("1", url);
//        intent.putExtra("2", title);
        context.startActivity(intent);
    }


    ProgressBarWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String url = getIntent().getStringExtra("1");
        System.out.println("url"+url);

//        String title = getIntent().getStringExtra("2");

        webView = findViewById(R.id.progressBarWebView1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setLogo(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<Cookie> cookies = ViseHttp.CONFIG().getApiCookie().loadForRequest(HttpUrl.parse(url));

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        for (Cookie cookie : cookies) {
            cookieManager.setCookie(url, String.format("%s=%s", cookie.name(), cookie.value()));//cookies是在HttpClient中获得的cookie
        }
        cookieManager.setCookie(url,"wechat_user_openidwxab0d22963d2598ac=oLAbKjnnkwOleatdPwyCAHyi9Kgo");
        CookieSyncManager.getInstance().sync();
        webView.loadUrl(url, App.getApp().getHeaders());
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
