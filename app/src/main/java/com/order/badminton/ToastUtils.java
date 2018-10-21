package com.order.badminton;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @类名:ToastUtils
 * @功能描述: Toast工具类
 * @作者:XuanKe'Huang
 * @时间:2015-3-30 下午5:13:57
 * @Copyright @2015
 */
@SuppressLint("InflateParams")
public class ToastUtils {
    /**
     * 显示自定义Toast
     *
     * @param mContext application mContext.Note:don't deliver the Activity Context avoid to memory
     * leak.
     * @param string string you want to show.
     */
    private static Toast toast;
//    private static int lastResid;


    public static void makeTextAndShow(String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(App.getApp(), string, Toast.LENGTH_SHORT);
        }
        toast.setText(string);
        toast.show();
    }

    public static void makeTextAndShow(String string, int drawableID) {
//        Context context = App.getContext();
//        if (Build.VERSION.SDK_INT >= 25) {
//            ToastN.show(context, string, ToastN.LENGTH_SHORT);
//        } else {
//            if (toast == null) {
//                View toastRoot = View.inflate(context, R.layout.view_mytoast, null);
//                toast = new Toast(context);
//                toast.setView(toastRoot);// 设置View
//                toast.setDuration(Toast.LENGTH_SHORT);// 设置时间
//            }
//            if (lastResid != drawableID) {
//                lastResid = drawableID;
//                TextView textView = (TextView) toast.getView();
//                if (drawableID != 0) {
//                    Tools.setTextViewDrawable(textView, drawableID, 1);
//                } else {
//                    textView.setCompoundDrawables(null, null, null, null);
//                }
//            }
//            try {
//                toast.setText(string);// 设置内容
//                toast.show();
//            } catch (Exception e) {
//                DataStatisticsUtils.reportError(context, e);
//            }
//        }


    }


//    public static void makeTextWithOK(String str) {
//        makeTextAndShow(str, R.drawable.ic_tixianok);
//
}
