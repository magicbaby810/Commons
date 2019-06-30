package com.sk.commons;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author sk on 2019/1/16.
 */
public class AppApplication extends MultiDexApplication {



    private static AppApplication instance;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        fixFinalizeTimeoutExce();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    // 解决oppo4.4到6.0系统为了流畅，熄屏、切后台后疯狂清内存，导致频繁GC
    public static void fixFinalizeTimeoutExce() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
