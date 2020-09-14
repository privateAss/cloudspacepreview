package com.ys100.yscloudpreview.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.ys100.yscloudpreview.PreViewFileActivity;
import com.ys100.yscloudpreview.bean.EventData;
import com.ys100.yscloudpreview.bean.EventToName;
import com.ys100.yscloudpreview.bean.PreviewCacheBean;
import com.ys100.yscloudpreview.data.PreviewCacheManager;
import com.ys100.yscloudpreview.listener.SendEventToHtml5Listener;

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
        initX5WebView(context);
    }

    /**
     * 初始化X5 WebView
     *
     * @param context
     */
    private static void initX5WebView(Context context) {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                if (arg0) {
                    Log.i("PreViewHelper", "X5 内核加载成功");
                } else {
                    Log.i("PreViewHelper", "X5 内核加载失败");
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
     *
     * @param context 上下文
     * @param baseIp baseIp okhttp的baseIp
     * @param eventData 事件返回的数据
     */
    public static void handleEventData(Context context,String baseIp,EventData eventData){
        if(eventData == null)return;
        handleEventData(context,baseIp,eventData.getEventName(),eventData.getObject());
    }

    public static void handleEventData(Context context, String baseIp, String eventName, JSONObject data) {
        if (TextUtils.isEmpty(eventName) || data == null) return;
        if (EventDataObserVer.getInstance().isHasObserver()) {
            EventDataObserVer.getInstance().notifyObservers(new EventData(eventName, data));
        } else {
            try {
                String pageName;
                String uuid;
                String url;
                switch (eventName) {
                    case EventToName.EVENT_CALLNATIVEPREVIEWINIT://初始化View
                        pageName = data.getString("pageName");
                        PreViewFileActivity.startPreViewFileActivity(context, data.toString(), pageName, baseIp);
                        break;
                    case EventToName.EVENT_READPREVIEWCACHEURL://读取缓存
                        uuid = data.getString("uuid");
                        pageName = data.getString("pageName");
                        PreviewCacheBean preViewCachByUUID = PreviewCacheManager.getInstance().setMMKV(context,baseIp).getPreViewCachByUUID(uuid);
                        url = "";
                        if (preViewCachByUUID != null)
                            url = preViewCachByUUID.getUrl();
                        JSONObject object = new JSONObject();
                        object.put("url", url);
                        object.put("uuid",uuid);
                        object.put("pageName",pageName);
                        SendEventToHtml5Listener html5Listener = YsPreViewEngine.getInstance().getHtml5Listener();
                        if(html5Listener != null)
                            html5Listener.sendEventToHtml5(EventToName.EVENT_READPREVIEWCACHEURLCOMPLETE,object);
                        break;
                    case EventToName.EVENT_WRITEPREVIEWCACHEURL://写入缓存
                        uuid = data.getString("uuid");
                        url = data.getString("url");
                        PreviewCacheManager.getInstance().setMMKV(context,baseIp).setPreviewCache(new PreviewCacheBean("",uuid,url,""));
                        break;
                }
            } catch (JSONException e) {
                Log.i("PreViewHelper", "解析数据失败:"+e.toString());
            }
        }
    }


}
