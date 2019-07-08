package com.sk.commons.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.sk.commons.R;

import java.io.ByteArrayOutputStream;

/**
 * @author sk on 2019/1/29.
 */
public class GlideUtils {


    /**
     * 获取通用的RequestOptions
     *
     */
    public static RequestOptions getOptions() {
        return new RequestOptions().placeholder(R.drawable.ic_photo_default_gray_44dp);
    }

    /**
     * 获取通用的头像RequestOptions
     *
     */
    public static RequestOptions getPortraitOptions() {
        return new RequestOptions().circleCrop().placeholder(R.drawable.ic_account_circle_white_24dp);
    }

    /**
     * 获取圆形的RequestOptions
     *
     */
    public static RequestOptions getRoundOptions(int radius) {
        return getOptions().bitmapTransform(new RoundedCorners(radius));
    }

    /**
     * 获取CenterCrop的RequestOptions
     *
     */
    public static RequestOptions getCenterCropOptions() {
        return getOptions().centerCrop();
    }

    /**
     * 获取不缓存的RequestOptions
     *
     */
    public static RequestOptions getNoCacheOptions() {
        String signature = SPUtils.get("glide_signature");
        if (TextUtils.isEmpty(signature)) {
            signature = System.currentTimeMillis()+"";
            SPUtils.put("glide_signature", signature);
        }
        return getOptions().signature(new ObjectKey(signature));
    }

    /**
     * 获取不缓存头像的RequestOptions
     *
     */
    public static RequestOptions getNoCachePortraitOptions() {
        String signature = SPUtils.get("glide_signature");
        if (TextUtils.isEmpty(signature)) {
            signature = System.currentTimeMillis()+"";
            SPUtils.put("glide_signature", signature);
        }
        return getPortraitOptions().signature(new ObjectKey(signature));
    }

    /**
     * 获取圆形的RequestOptions
     *
     */
    public static RequestOptions getCircleOptions() {
        return getOptions().circleCrop();
    }

    /**
     * 获取不缓存圆形的RequestOptions
     *
     */
    public static RequestOptions getNoCacheCircleOptions() {
        return getNoCacheOptions().circleCrop();
    }

}
