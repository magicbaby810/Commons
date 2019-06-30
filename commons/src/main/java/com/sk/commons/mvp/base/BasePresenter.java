package com.sk.commons.mvp.base;

import android.content.Context;

import com.sk.commons.utils.RxManager;


/**
 * des:基类presenter
 * T:mView
 * E:mModel
 */
public abstract class BasePresenter<T, E> {
    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManager = new RxManager();

    public void setVM(T mView, E mModel) {
        this.mView = mView;
        this.mModel = mModel;
        this.onStart();
    }

    public void onStart() {
    }

    public void onDestroy() {
        mRxManager.clear();
    }
}
