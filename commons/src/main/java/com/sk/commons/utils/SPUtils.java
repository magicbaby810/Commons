package com.sk.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.sk.commons.BaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * 简化SharedPreferences的帮助类
 */
public class SPUtils {


    private static SharedPreferences sp;
    private static String FILE_NAME = "cache";

    private static SharedPreferences getSharedPreferences() {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 设置sp文件名,在application中设置一次即可一次即可。
     *
     * @param file_name 保存的sp文件名
     */
    public static void setFileNmae(String file_name) {
        FILE_NAME = file_name;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public synchronized static void put(String key, Object object) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if (object == null) {
            Log.e("SPUtils", "文件写入失败,请检查参数设置" + " key : " + key);
            return;
        }
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public synchronized static String get(String key) {
        return String.valueOf(get(key, ""));
    }

    public synchronized static int getInt(String key) {
        return (int) get(key, 0);
    }

    public synchronized static boolean getBool(String key) {
        return (boolean) get(key, false);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     *
     * @return
     */
    public synchronized static Object get(String key, Object defaultObject) {

        if (defaultObject instanceof String) {
            return getSharedPreferences().getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return getSharedPreferences().getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return getSharedPreferences().getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return getSharedPreferences().getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return getSharedPreferences().getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public synchronized static void remove(String key) {

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public synchronized static void clear() {

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     *
     * @return
     */
    public synchronized static boolean contains(String key) {

        return getSharedPreferences().contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public synchronized static Map<String, ?> getAll() {

        return getSharedPreferences().getAll();
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public synchronized static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }

}