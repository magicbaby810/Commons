package com.sk.commons.mvp.base;

public interface BaseView {

    /**
     * 请求结束
     */
    void returnFinish();

    /**
     * token失效
     */
    void returnTokenInvalid();

    /**
     * 返回错误信息
     * @param msg
     */
    void onError(String msg);

    /**
     * 返回从哪个接口返回的错误信息
     * @param interf
     * @param msg
     */
    void onError(int interf, String msg);
}
