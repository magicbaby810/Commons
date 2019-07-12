package com.sk.commons.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sk.commons.R;
import com.sk.commons.mvp.TransformMvpUtils;
import com.sk.commons.mvp.base.BaseModel;
import com.sk.commons.mvp.base.BasePresenter;
import com.sk.commons.utils.RxManager;

import butterknife.ButterKnife;

import static com.sk.commons.mvp.TransformMvpUtils.PARAM_INDEX_ONE;
import static com.sk.commons.mvp.TransformMvpUtils.PARAM_INDEX_ZERO;


/**
  *@desc
  *@author sk
  *@data 2019/4/30
  */
public abstract class DataBindingFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {

    protected View rootView;
    public T mPresenter;
    public E mModel;
    protected RxManager mRxManager;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = getLayoutView(inflater, container);

            initLayout(rootView, savedInstanceState);
        }

        mRxManager = new RxManager();
        ButterKnife.bind(this, rootView);
        mPresenter = TransformMvpUtils.getT(this, PARAM_INDEX_ZERO);
        mModel = TransformMvpUtils.getT(this, PARAM_INDEX_ONE);
        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
        }

        initPresenter();
        initView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    protected void initLayout(View v, Bundle savedInstanceState) {

    }

    /** 获取布局view */
    protected abstract View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    /** 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通 */
    public abstract void initPresenter();

    /** 初始化view */
    protected abstract void initView();



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    /**
     * 显示进度条
     */
    public void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity(), R.style.progress_dialog_style);
        }
        //解决  android.view.WindowManager$BadTokenException问题
        if (!progressDialog.isShowing() && !getActivity().isFinishing()) {
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }
    }

    /**
     * 关闭进度条
     */
    public void dismissProgressBar() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * don't close current activity
     *
     * @param clz
     * @param ex
     */
    public void gotoActivity(Class<?> clz, Bundle ex) {
        gotoActivity(clz, false, ex);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     *
     * @param clz
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(getActivity(), clz);
        if (ex != null) {
            intent.putExtras(ex);
        }
        startActivity(intent);
        if (isCloseCurrentActivity) {
            getActivity().finish();
        }
    }
}
