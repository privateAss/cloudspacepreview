package com.ys100.yscloudpreview.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ys100.yscloudpreview.listener.ActivityAddOrRemoveListener;
import com.ys100.yscloudpreview.manager.SuperWebManager;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/21
 * Description:
 */
public abstract class BaseYsActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResLayoutId());
        ActivityAddOrRemoveListener listener = SuperWebManager.getInstance().getListener();
        if(listener != null){
            listener.onAddActivity(this);
        }
        initView();
    }


    private void initView() {
        initViews();
        initLister();
        initData();
    }

    protected abstract int getResLayoutId();

    /**
     * 子类的一些初始化View
     */
    protected abstract void initViews();

    /**
     * 子类View的一些监听事件
     */
    protected abstract void initLister();


    /**
     * 子类的一些数据实现
     */
    protected abstract void initData();


    @Override
    protected void onDestroy() {
        ActivityAddOrRemoveListener listener = SuperWebManager.getInstance().getListener();
        if(listener != null){
            listener.onRemoveActivity(this);
        }
        super.onDestroy();
    }
}
