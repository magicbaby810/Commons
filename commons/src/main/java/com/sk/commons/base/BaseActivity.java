package com.sk.commons.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.sk.commons.R;
import com.sk.commons.mvp.TransformMvpUtils;
import com.sk.commons.mvp.base.BaseModel;
import com.sk.commons.mvp.base.BasePresenter;
import com.sk.commons.utils.ActivityManager;
import com.sk.commons.utils.SoftKeyBoardListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sk.commons.mvp.TransformMvpUtils.PARAM_INDEX_ONE;
import static com.sk.commons.mvp.TransformMvpUtils.PARAM_INDEX_ZERO;


/**
 * @author sk on 2019/1/15.
 */
public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {


    public T mPresenter;
    public E mModel;

    protected boolean isShowingKeyboard = false;
    protected String TAG = getClass().getSimpleName();

    /**
     * 设置布局文件
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化presenter
     */
    protected abstract void initPresenter();

    /**
     * 设置状态栏颜色
     * @return
     */
    protected abstract int setStatusBarColor();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 加载数据信息
     */
    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mPresenter = TransformMvpUtils.getT(this, PARAM_INDEX_ZERO);
        mModel = TransformMvpUtils.getT(this, PARAM_INDEX_ONE);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        initPresenter();

        setAndroidNativeLightStatusBar(this, false);

        initView();
        initData();

        ActivityManager.getInstance().addActivity(this);

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener(){

            @Override
            public void keyBoardShow(int height) {
                isShowingKeyboard = true;
            }

            @Override
            public void keyBoardHide(int height) {
                isShowingKeyboard = false;
            }
        });
    }

    private void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (setStatusBarColor() == 0) {
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.text_color_f0));
            } else {
                window.setStatusBarColor(ContextCompat.getColor(this, setStatusBarColor()));
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.getAttributes().flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | window.getAttributes().flags);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }

        Glide.get(this).clearMemory();
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) {
            intent.putExtras(ex);
        }
        startActivity(intent);
        if (isCloseCurrentActivity) {
            this.finish();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
