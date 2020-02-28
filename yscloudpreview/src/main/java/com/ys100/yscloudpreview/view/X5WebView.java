package com.ys100.yscloudpreview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * =======================================
 *
 * @author: Created by xianglei
 * @Date: 2019/10/11 0011
 * @Edition：1.0
 * @Description:
 * @ModifyDescription ： 自定义TBS的WebView
 * <p>
 * ========================================
 */
public class X5WebView extends WebView {
    private Context mContext;
    private WebViewClient client;
    private WebChromeClient chromeClient;

    public X5WebView(Context context) {
        this(context, null);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        if (getX5WebViewExtension() != null)
            getX5WebViewExtension().setScrollBarFadingEnabled(false);
        setHorizontalScrollBarEnabled(false);//水平不显示小方块
        setVerticalScrollBarEnabled(false); //垂直不显示小方块
        initWebViewSettings();
    }

    public X5WebView setClient(WebViewClient client) {
        if (client != null)
            setWebViewClient(client);
        return this;
    }

    public X5WebView setChromeClient(WebChromeClient chromeClient) {
        if (chromeClient != null)
            setWebChromeClient(chromeClient);
        return this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        setBackgroundColor(getResources().getColor(android.R.color.white));
        if (client != null)
            setWebViewClient(client);
        if (chromeClient != null)
            setWebChromeClient(chromeClient);
        setClickable(true);
        setOnTouchListener((v, event) -> false);
        WebSettings webSetting = getSettings();

        webSetting.setSavePassword(false);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setAllowFileAccess(false);
        webSetting.setAllowUniversalAccessFromFileURLs(false);
        webSetting.setAllowFileAccessFromFileURLs(false);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setBlockNetworkImage(false);//解决图片不显示
        webSetting.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
            this.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 这里可以为自定义webview绘制背景或文字
     *
     * @param canvas
     * @param child
     * @param drawingTime
     * @return
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
        canvas.save();
        return ret;
    }
}
