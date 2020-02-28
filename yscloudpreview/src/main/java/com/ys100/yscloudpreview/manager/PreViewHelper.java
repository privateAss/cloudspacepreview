package com.ys100.yscloudpreview.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.uzmap.pkg.openapi.APICloud;
import com.uzmap.pkg.openapi.ExternalActivity;
import com.uzmap.pkg.openapi.SuperWebview;
import com.ys100.yscloudpreview.PreViewFileActivity;
import com.ys100.yscloudpreview.bean.FilePreviewBean;
import com.ys100.yscloudpreview.bean.PreviewCacheBean;
import com.ys100.yscloudpreview.bean.WebViewEvent;
import com.ys100.yscloudpreview.data.PreviewCacheManager;
import com.ys100.yscloudpreview.listener.ActivityAddOrRemoveListener;
import com.ys100.yscloudpreview.utils.CommonUtils;
import com.ys100.yscloudpreview.utils.GsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class PreViewHelper {

    public static void init(Context context) {
        APICloud.initialize(context);
        initX5WebView(context);
    }

    private static void initX5WebView(Context context) {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                if (arg0) {
                    Log.i("ArticleSystem", "X5 内核加载成功");
                } else {
                    Log.i("ArticleSystem", "X5 内核加载失败");
                }
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
    }


    /**
     * 家校通调用
     *
     * @param context   上下文
     * @param eventName 事件名称
     * @param object    事件所带的参数
     * @param listener  跳转的activity 添加/移除监听事件
     */
    public static void onReceive(Context context, String eventName, JSONObject object, String baseIp, ActivityAddOrRemoveListener listener) {
        onReceive(context, eventName, object, baseIp, listener, null);
    }

    public static void onReceive(Context context, String eventName, JSONObject object, String baseIp, ActivityAddOrRemoveListener listener, SuperWebview webview) {
        if (object == null) return;
        //设置承载url的对象
        SuperWebManager.getInstance().setApiCloudObject(webview == null ? context : webview).setListener(listener);
        try {
            String pageName = object.has("pageName") ? object.getString("pageName") : "";
            //存储pageName 为了后面的使用
            if (!TextUtils.isEmpty(pageName))
                PreviewCacheManager.getInstance().setMMKV(context, baseIp).setPageName(pageName);

            String uuid = object.has("uuid") ? object.getString("uuid") : "";
            String url = object.has("url") ? object.getString("url") : "";
            if (CommonUtils.isAPPBroughtToBackground(context)) {//activity在前台
                switch (eventName) {
                    case WebViewEvent.EVENT_INITPREVIEW://初始化预览
                        PreViewFileActivity.startPreViewFileActivity(context, object.toString(), pageName);
                        break;
                    case WebViewEvent.EVENT_WRITEPREVIEWCACHEURL://写入存储
                        PreviewCacheManager.getInstance().setMMKV(context, baseIp).setPreviewCache(new PreviewCacheBean("", uuid, url, ""));
                        break;
                    case WebViewEvent.EVENT_READPREVIEWCACHEURL://读取存储
                        readInCache(context, baseIp, uuid);
                        break;
                }
            } else {
                switch (eventName) {
                    //通过观察者模式通知页面更新
                    case WebViewEvent.EVENT_RESPONSEPREVIEWURL://点击上一页/下一页页面事件响应
                        FilePreviewBean previewSuccessBean = GsonHelper.toObject(object.toString(),FilePreviewBean.class);
                        previewSuccessBean.setStatus(FilePreviewBean.SUCCESS);
                        break;
                    case WebViewEvent.EVENT_CALLNATIVEPREVIEWSHOWERROR://切换页面出错
                        FilePreviewBean previewErrorBean = GsonHelper.toObject(object.toString(),FilePreviewBean.class);
                        previewErrorBean.setStatus(FilePreviewBean.UNSUCCESS);
                        break;
                    case WebViewEvent.EVENT_READPREVIEWCACHEURL://读取存储
                        PreviewCacheManager.getInstance().setMMKV(context, baseIp).setPreviewCache(new PreviewCacheBean("", uuid, url, ""));
                        break;
                    case WebViewEvent.EVENT_WRITEPREVIEWCACHEURL://写入存储
                        readInCache(context, baseIp, uuid);
                        break;
                }
            }
        } catch (JSONException e) {

        }
    }

    /**
     * 读取缓存及发送事件告诉H5
     *
     * @param context
     * @param baseIp
     * @param uuid
     * @throws JSONException
     */
    private static void readInCache(Context context, String baseIp, String uuid) throws JSONException {
        PreviewCacheManager manager = PreviewCacheManager.getInstance().setMMKV(context, baseIp);
        PreviewCacheBean preViewCacheByUUID = manager.getPreViewCachByUUID(uuid);
        String url = "";
        if (preViewCacheByUUID != null)
            url = preViewCacheByUUID.getUrl();
        JSONObject object = new JSONObject();
        object.put("url", url);
        object.put("uuid", uuid);
        object.put("pageName", manager.getPageName());
        SuperWebManager.getInstance().setH5Event(WebViewEvent.EVENT_READPREVIEWCACHEURLCOMPLETE,object);
    }


}
