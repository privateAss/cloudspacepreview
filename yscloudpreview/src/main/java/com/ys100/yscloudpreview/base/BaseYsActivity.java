package com.ys100.yscloudpreview.base;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.ys100.yscloudpreview.R;
import com.ys100.yscloudpreview.listener.ActivityAddOrRemoveListener;
import com.ys100.yscloudpreview.manager.YsPreViewEngine;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/21
 * Description:
 */
public abstract class BaseYsActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate{
    protected final String TAG = this.getClass().getSimpleName();
    protected ImmersionBar immersionBar;
    //侧滑关闭控件
    protected BGASwipeBackHelper mSwipeBackHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        setContentView(getResLayoutId());
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(false).autoDarkModeEnable(true).transparentStatusBar().init();
        LinearLayout linearLayout = findViewById(R.id.ll_title);
        if(linearLayout != null){
            int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
            linearLayout.setPadding(0,statusBarHeight,0,0);
        }
        ActivityAddOrRemoveListener listener = YsPreViewEngine.getInstance().getAddOrRemoveListener();
        if (listener != null) {
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
        ActivityAddOrRemoveListener listener = YsPreViewEngine.getInstance().getAddOrRemoveListener();
        if (listener != null) {
            listener.onRemoveActivity(this);
        }
        super.onDestroy();
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
}
