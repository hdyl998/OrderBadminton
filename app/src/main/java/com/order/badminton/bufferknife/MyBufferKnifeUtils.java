package com.order.badminton.bufferknife;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/4/17.
 */

public class MyBufferKnifeUtils {
    public static void inject(Object object, View view) {
        try {
            Field fields[] = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                MyBindView bindView = field.getAnnotation(MyBindView.class);
                if (bindView != null) {
                    field.setAccessible(true);
                    field.set(object, view.findViewById(bindView.value()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void inject(Activity activity) {
        inject(activity, activity.getWindow().getDecorView());
    }

    public static void inject(Fragment fragment) {
        inject(fragment, fragment.getView());
    }
}
