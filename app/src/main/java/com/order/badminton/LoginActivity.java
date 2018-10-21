package com.order.badminton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.vise.xsnow.cache.SpCache;
import com.vise.xsnow.http.ViseHttp;

public class LoginActivity extends AppCompatActivity {


    EditText editAccount, editPwd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setLogo(null);
        toolbar.setTitle("登录");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        editAccount = findViewById(R.id.editText);
        editPwd = findViewById(R.id.editText2);
        editAccount.setText(spCache.get("moblie", ""));
        editPwd.setText(spCache.get("pwd", ""));

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAccount.length() != 0 && editPwd.length() != 0) {
                    doLogin();
                } else {
                    ToastUtils.makeTextAndShow("账号密码不能为空！");
                }
            }
        });
    }

    SpCache spCache = new SpCache(App.getApp(), "logininfo");

    private void doLogin() {
        String mobile = editAccount.getText().toString().trim();
        String pwd = editPwd.getText().toString().trim();

        spCache.put("moblie", mobile);
        spCache.put("pwd", pwd);

        ViseHttp.POST("login.do")
                .baseUrl("http://lgtyzx.ydmap.com.cn/v2/pubUser/")
                .addForm("mobile", mobile)
                .addForm("pwd", pwd).request(new MyActionCallback<String>() {


            @Override
            public void onSuccessFinal(NetEntity entity) {
                ToastUtils.makeTextAndShow(entity.msg);
                getUserInfo();
            }
        });


    }


    public void getUserInfo() {
        ViseHttp.GET("http://lgtyzx.ydmap.com.cn/v2/pubUser/my.do?t=" + DateUtils.getCurrentMills())
                .request(new MyActionCallback<String>() {
                    @Override
                    public void onSuccessFinal(NetEntity entity) {
                        UserInfoItem infoItem = JSON.parseObject(entity.data, UserInfoItem.class);
                        App.getApp().infoItem = infoItem;
                        spCache.put("userinfo", JSON.toJSONString(infoItem));
                        finish();
                    }
                });
    }


}
