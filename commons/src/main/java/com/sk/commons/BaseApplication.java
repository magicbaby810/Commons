package com.sk.commons;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.sk.commons.utils.SPUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.plugins.RxJavaPlugins;


/**
 * @author sk on 2019/1/16.
 */
public class BaseApplication extends MultiDexApplication {

    // 初始化通知channel
    public static final String CHANNEL_ID_KEY = "通知";

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
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

        initChannel();

        // The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with.
        // RxJava在处理exception时订阅已经被解除，无法找到消费者去处理exception，exception无处可去，线程大量积压，栈内存无法释放
        // 而华为部分手机线程数阈值较低，很容易就导致线程爆棚，导致栈内存溢出，pthread_create (1040KB stack) failed: Out of memory
        // 从bugly可以看到此类问题都出现在华为固定的几款机型上
        RxJavaPlugins.setErrorHandler(t -> Log.e("Rx error", "Unhandled Rx error " + (null != t ?  ("-->> " + t.getMessage()) : "")));
    }

    /** 初始化通知channel 兼容8.0以上的系统 */
    public void initChannel() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && null != nm) {
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID_KEY, "通知", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    /** 解决oppo4.4到6.0系统为了流畅，熄屏、切后台后疯狂清内存，导致频繁GC */
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
