package com.sk.commons.mvp;

import java.lang.reflect.ParameterizedType;

/**
 * 类转换初始化
 */
public class TransformMvpUtils {

    public static final int PARAM_INDEX_ZERO = 0;
    public static final int PARAM_INDEX_ONE = 1;

    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassCastException e) {
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
