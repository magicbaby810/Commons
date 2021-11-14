package com.sk.commons.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.sk.commons.R;


/**
 * Created by Wyman on 15/12/31.
 */
public class ToastUtils {
    /**
     * 是否显示Toast,可在application初始化，默认为显示--true
     */
    private static boolean isShow = true;

    private ToastUtils() {
        /** cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast str
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        try {
            if (isShow) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 短时间显示Toast int
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        try {
            if (isShow) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 长时间显示Toast str
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        try {
            if (isShow) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 长时间显示Toast int
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        try {
            if (isShow) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义显示Toast时间 str
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showCustom(Context context, CharSequence message, int duration) {
        try {
            if (isShow) {
                Toast.makeText(context, message, duration).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCustomView(Context context, String content) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null, false);
        AppCompatTextView contentText = view.findViewById(R.id.content);
        contentText.setText(content);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
    public static void showCustomViewWrap(Context context, String content) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast_wrap, null, false);
        AppCompatTextView contentText = view.findViewById(R.id.content);
        contentText.setText(content);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
    /**
     * 自定义Img
     * @param context
     * @param resourceId
     */
    public static void showCustomImageView(Context context, int resourceId) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_img_toast, null, false);
        AppCompatImageView img = view.findViewById(R.id.img);
        img.setImageResource(resourceId);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 自定义Img
     * @param context
     * @param resourceId
     */
    public static void showCustomImageView(Context context, int resourceId,String title) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_img_title_toast, null, false);
        AppCompatImageView img = view.findViewById(R.id.img);
        TextView tvc=view.findViewById(R.id.tv_toast_title);
        tvc.setText(title);
        img.setImageResource(resourceId);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 在底部弹出
     * @param context
     * @param resourceId
     */
    public static void showCustomImageViewBottom(Context context, int resourceId,String title) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_img_title_toast, null, false);
        AppCompatImageView img = view.findViewById(R.id.img);
        TextView tvc=view.findViewById(R.id.tv_toast_title);
        tvc.setText(title);
        img.setImageResource(resourceId);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, AppUtils.dip2px(context,80));
        toast.show();
    }

    /**
     * 自定义Img
     *
     * @param context
     * @param resourceId
     */
    public static void showLoginFailureView(Context context, int resourceId, String title) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_login_failure_toast, null, false);
        AppCompatImageView img = view.findViewById(R.id.img);
        TextView tvc = view.findViewById(R.id.tv_toast_title);
        tvc.setText(title);
        img.setImageResource(resourceId);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
    }

    /**
     * 自定义显示Toast时间 int
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showCustom(Context context, int message, int duration) {
        try {
            if (isShow) {
                Toast.makeText(context, message, duration).show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认str显示Toast int
     *
     * @param context
     * @param message
     */
    public static void show(Context context, int message) {
        try {
            if (isShow) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认str显示Toast String
     *
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {
        try {
            if (isShow) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
