package com.ys100.yscloudpreview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.ys100.yscloudpreview.base.BaseYsActivity;
import com.ys100.yscloudpreview.bean.EventData;
import com.ys100.yscloudpreview.bean.FilePreviewBean;
import com.ys100.yscloudpreview.bean.EventToName;
import com.ys100.yscloudpreview.bean.PreviewCacheBean;
import com.ys100.yscloudpreview.data.PreviewCacheManager;
import com.ys100.yscloudpreview.listener.SendEventToHtml5Listener;
import com.ys100.yscloudpreview.manager.EventDataObserVer;
import com.ys100.yscloudpreview.manager.YsPreViewEngine;
import com.ys100.yscloudpreview.utils.FilePreviewIntentFactory;
import com.ys100.yscloudpreview.utils.GlideHelper;
import com.ys100.yscloudpreview.utils.GsonHelper;
import com.ys100.yscloudpreview.view.X5PerWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/21
 * Description:
 */
public class PreViewFileActivity extends BaseYsActivity implements View.OnClickListener, RequestListener<Bitmap>,Observer {
    /**
     * 跳转到预览文件界面并且传递必要的参数
     *
     * @param context     上下文
     * @param preViewJson 预览文件的json
     * @param pageName    预览页面的别名  （例如 个人空间/群组空间等等）
     */
    public static void startPreViewFileActivity(Context context, String preViewJson, String pageName, String baseIp) {
        //传递数据
        Bundle mBundle = new Bundle();
        mBundle.putString(PAGE_PREVIEW_JSON, preViewJson);
        mBundle.putString(PAGE_NAME_KEY, pageName);
        mBundle.putString(APP_BASE_IP, baseIp);
        Intent intent = new Intent(context, PreViewFileActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public final static String PAGE_NAME_KEY = "pageName";
    public final static String PAGE_PREVIEW_JSON = "previewJson";
    public final static String APP_BASE_IP = "baseIp";
    private FilePreviewBean mPreviewBean;//预览文件的实体
    private int total;//文件的总数

    private int currentCursor;//当前的位置

    private TextView tv_number;
    private TextView tv_title;
    private ImageView iv_close;
    private ImageView iv_left_next;
    private ImageView iv_right_next;
    private PhotoView photo_view;
    private X5PerWebView x5_web;

    /********视频/音频**********/
    private FrameLayout fl_paly;
    private ImageView iv_thumbnail;//缩略图
    private ImageView iv_play;//视频、音频播放按钮

    /***********出错啦************/
    private LinearLayout ll_error;
    private TextView tv_msg;//错误提示
    private Button bt_reload;//重新加载按钮

    /**********进度条********/
    private ImageView iv_loading;
    private Animation mAnimation;
    private String pageName;
    private String baseIp;

    @Override
    protected int getResLayoutId() {
        return R.layout.activity_preview_file;
    }

    @Override
    protected void initViews() {
        //初始化Id
        tv_number = findViewById(R.id.tv_number);
        tv_title = findViewById(R.id.tv_title);
        iv_close = findViewById(R.id.iv_close);
        iv_left_next = findViewById(R.id.iv_left_next);
        iv_right_next = findViewById(R.id.iv_right_next);
        photo_view = findViewById(R.id.photo_view);
        x5_web = findViewById(R.id.x5_web);
        fl_paly = findViewById(R.id.fl_play);
        iv_thumbnail = findViewById(R.id.iv_thumbnail);
        iv_play = findViewById(R.id.iv_play);
        ll_error = findViewById(R.id.ll_error);
        tv_msg = findViewById(R.id.tv_msg);
        bt_reload = findViewById(R.id.bt_reload);
        iv_loading = findViewById(R.id.iv_loading);
    }

    @Override
    protected void initLister() {
        //设置监听
        iv_close.setOnClickListener(this);
        iv_left_next.setOnClickListener(this);
        iv_right_next.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        bt_reload.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        EventDataObserVer.getInstance().addObserver(this);
        x5_web.setChromeClient(chromeClient).setClient(mViewClient);
        mAnimation = FilePreviewIntentFactory.animation(this);
        Intent intent = getIntent();
        if (intent.hasExtra(PAGE_NAME_KEY)) {
            pageName = intent.getStringExtra(PAGE_NAME_KEY);
        }
        if (intent.hasExtra(APP_BASE_IP)) {
            baseIp = intent.getStringExtra(APP_BASE_IP);
        }
        if (intent.hasExtra(PAGE_PREVIEW_JSON)) {
            String previewJsons = intent.getStringExtra(PAGE_PREVIEW_JSON);
            if (TextUtils.isEmpty(previewJsons)) return;
            mPreviewBean = GsonHelper.toObject(previewJsons, FilePreviewBean.class);
            if (mPreviewBean != null) {
                total = mPreviewBean.getTotal();
                currentCursor = mPreviewBean.getCurrIndex();
                setTitleAndNumber(mPreviewBean, currentCursor);
                if (TextUtils.isEmpty(mPreviewBean.getUrl())) {
                    startLoading();
                    sendEvent(mPreviewBean.getCurrIndex());
                } else {
                    mPreviewBean.setStatus(FilePreviewBean.SUCCESS);
                    previewControl(mPreviewBean);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setTitleAndNumber(FilePreviewBean bean, int mCurrentCurson) {
        tv_number.setText(String.format(getResources().getString(R.string.preview_number), mCurrentCurson, total));
        tv_title.setText(bean.getName());
        iv_left_next.setImageResource(R.mipmap.ic_arrowl_p);
        iv_right_next.setImageResource(R.mipmap.ic_arrowr_p);
        iv_left_next.setEnabled(true);
        iv_right_next.setEnabled(true);
        if (bean.isLastLeft(mCurrentCurson)) {
            iv_left_next.setImageResource(R.mipmap.ic_arrowl_n);
            iv_left_next.setEnabled(false);
        }
        if (bean.isLastRight(mCurrentCurson, total)) {
            iv_right_next.setImageResource(R.mipmap.ic_arrowr_n);
            iv_right_next.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
            finish();
        } else if (id == R.id.iv_left_next) {
            currentCursor -= 1;
            sendEvent(currentCursor);
            setTitleAndNumber(mPreviewBean, currentCursor);
        } else if (id == R.id.iv_right_next) {
            currentCursor += 1;
            sendEvent(currentCursor);
            setTitleAndNumber(mPreviewBean, currentCursor);
        } else if (id == R.id.iv_play) {
            FilePreviewIntentFactory.startAudio(mPreviewBean.getUrl(), mPreviewBean.getName(), this);
        } else if (id == R.id.bt_reload) {//重新加载
            //这儿需要判断是否获取到url 然后再次判断是否发生事件
            sendEvent(mPreviewBean.getCurrIndex());
            setTitleAndNumber(mPreviewBean, currentCursor);
        }
    }


    /**
     * 发送事件
     *
     * @param cursor 当前的游标
     */
    private void sendEvent(int cursor) {
        startLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("currIndex", cursor);
            object.put(PAGE_NAME_KEY, pageName);
            SendEventToHtml5Listener listener = YsPreViewEngine.getInstance().getHtml5Listener();
            if (listener != null) {
                listener.sendEventToHtml5(EventToName.EVENT_CHANGENATIVEPREVIEWPAGE, object);
            }
        } catch (JSONException e) {
            if (mPreviewBean != null) {
                mPreviewBean.setCurrIndex(cursor);
                mPreviewBean.setError("发送事件失败");
                mPreviewBean.setStatus("error");
                previewControl(mPreviewBean);
            }
        }
    }


    /**
     * 预览控制
     *
     * @param previewBean 当前的文件实体类
     */
    private void previewControl(FilePreviewBean previewBean) {
        setAllGone(previewBean.isDoc());
        if (previewBean.isSuccess()) {
            //文件获取成功处理
            if (previewBean.isImage()) {//图片
                stopLoading();
                photo_view.setVisibility(View.VISIBLE);
                GlideHelper.loadRoundImageByListener(this, previewBean.getUrl(), photo_view, this, false);
            } else if (previewBean.isDoc()) {//文件
                x5_web.loadUrl(previewBean.getUrl());
            } else if (previewBean.isVideoOrAudio()) {//视频及音频
                fl_paly.setVisibility(View.VISIBLE);
                stopLoading();
                GlideHelper.loadRoundVideoByListener(this, previewBean.getUrl(), iv_thumbnail, this, true);
            } else {
                onLoadingError();
            }
        } else {
            onLoadingError();
        }

    }

    /**********doc页面处理********/
    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView webView, int progress) {
            if (progress > 80) {
                x5_web.setVisibility(View.VISIBLE);
                stopLoading();
            }
        }
    };


    private WebViewClient mViewClient = new WebViewClient() {


        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            x5_web.setVisibility(View.VISIBLE);
            stopLoading();
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            x5_web.setVisibility(View.VISIBLE);
            stopLoading();
        }
    };

    private void startLoading() {
        setAllGone(false);
        iv_loading.setVisibility(View.VISIBLE);
        if (mAnimation != null) {
            FilePreviewIntentFactory.startAnimation(mAnimation, iv_loading);
        }
    }

    public void stopLoading() {
        iv_loading.setVisibility(View.GONE);
        if (mAnimation != null) {
            FilePreviewIntentFactory.stopAnimation(iv_loading);
        }
    }

    private void onLoadingError() {
        stopLoading();
        //文件获取失败处理
        ll_error.setVisibility(View.VISIBLE);
        if (mPreviewBean != null && !TextUtils.isEmpty(mPreviewBean.getError())) {
            tv_msg.setText(mPreviewBean.getError());
        } else {
            tv_msg.setText("内容加载失败，请重新加载！");
        }
    }

    private void setAllGone(boolean isDoc) {
        photo_view.setVisibility(View.GONE);
        x5_web.setVisibility(View.GONE);
        fl_paly.setVisibility(View.GONE);
        ll_error.setVisibility(View.GONE);
        if (!isDoc)
            iv_loading.setVisibility(View.GONE);
    }

    //图片加载失败
    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
        return false;
    }

    //图片加载成功
    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        return false;
    }

    @Override
    protected void onDestroy() {
        if (x5_web != null) {
            x5_web.destroy();
        }
        EventDataObserVer.getInstance().removeObserver(this);
        super.onDestroy();
    }
    @Override
    public void update(Observable o, Object arg) {
        if(arg != null && arg instanceof EventData){
            handleEvent((EventData) arg);
        }
    }
    public void handleEvent(EventData data) {
        String url = "";
        String uuid ="";
        try {
            String eventName = data.getEventName();
            JSONObject object = data.getObject();
            if (TextUtils.isEmpty(eventName) || object == null) return;
            switch (eventName) {
                case EventToName.EVENT_CALLNATIVEPREVIEWCHANGEPAGE:
                    //真正返回预览url/数据的事件
                    pageName = object.getString(PAGE_NAME_KEY);
                    mPreviewBean = GsonHelper.toObject(object.toString(), FilePreviewBean.class);
                    mPreviewBean.setStatus(FilePreviewBean.SUCCESS);
                    if (mPreviewBean != null) {
                        setTitleAndNumber(mPreviewBean, currentCursor);
                        previewControl(mPreviewBean);
                    }
                    break;
                case EventToName.EVENT_CALLNATIVEPREVIEWSHOWERROR:
                    //返回出错的事件
                    mPreviewBean = GsonHelper.toObject(object.toString(), FilePreviewBean.class);
                    mPreviewBean.setStatus(FilePreviewBean.UNSUCCESS);
                    if (mPreviewBean != null) {
                        setTitleAndNumber(mPreviewBean, currentCursor);
                        previewControl(mPreviewBean);
                    }
                    break;
                case EventToName.EVENT_READPREVIEWCACHEURL:
                    //读取缓存
                    uuid = object.getString("uuid");
                    pageName = object.getString(PAGE_NAME_KEY);
                    PreviewCacheBean preViewCachByUUID = PreviewCacheManager.getInstance().setMMKV(this, baseIp).getPreViewCachByUUID(uuid);
                    if (preViewCachByUUID != null)
                        url = preViewCachByUUID.getUrl();
                    JSONObject objectToH5 = new JSONObject();
                    objectToH5.put("url", url);
                    objectToH5.put("uuid", uuid);
                    objectToH5.put(PAGE_NAME_KEY, pageName);
                    SendEventToHtml5Listener listener = YsPreViewEngine.getInstance().getHtml5Listener();
                    if (listener != null) {
                        listener.sendEventToHtml5(EventToName.EVENT_READPREVIEWCACHEURLCOMPLETE, objectToH5);
                    }
                    break;
                case EventToName.EVENT_WRITEPREVIEWCACHEURL:
                    //写入缓存
                    uuid = object.getString("uuid");
                    url = object.getString("url");
                    PreviewCacheManager.getInstance().setMMKV(this, baseIp).setPreviewCache(new PreviewCacheBean("", uuid, url, ""));
                    break;
            }
        } catch (JSONException e) {

        }
    }


}
