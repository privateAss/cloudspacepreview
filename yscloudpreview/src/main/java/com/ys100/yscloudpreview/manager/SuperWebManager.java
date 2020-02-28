package com.ys100.yscloudpreview.manager;

import com.uzmap.pkg.openapi.APIListener;
import com.uzmap.pkg.openapi.ExternalActivity;
import com.uzmap.pkg.openapi.Html5EventListener;
import com.uzmap.pkg.openapi.SuperWebview;
import com.uzmap.pkg.openapi.WebViewProvider;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.ys100.yscloudpreview.listener.ActivityAddOrRemoveListener;
import com.ys100.yscloudpreview.listener.Html5ReceiveListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observer;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description: we
 */
public class SuperWebManager {

    /**
     * apiObject 有可能是ExternalActivity / SuperWebView
     */
    private Object apiObject;

    //添加或者移除activity监听
    private ActivityAddOrRemoveListener listener;

    //观察者模式更新预览页面更新数据
    private ArrayList<Observer> observers = new ArrayList<>();

    public SuperWebManager setApiCloudObject(Object target) {
        this.apiObject = target;
        return this;
    }

    public Object getApiObject() {
        return apiObject;
    }

    public SuperWebManager setListener(ActivityAddOrRemoveListener listener) {
        this.listener = listener;
        return this;
    }

    public ActivityAddOrRemoveListener getListener() {
        return listener;
    }

    public void setH5Event(String eventName, JSONObject object) {
        if (apiObject == null) return;
        if (apiObject instanceof SuperWebview) {
            ((SuperWebview) apiObject).sendEventToHtml5(eventName, object);
        } else if (apiObject instanceof ExternalActivity) {
            ((ExternalActivity) apiObject).sendEventToHtml5(eventName, object);
        }
    }

    private static class SuperWebManagerHolder {
        private static SuperWebManager holder = new SuperWebManager();
    }

    public static SuperWebManager getInstance() {
        return SuperWebManagerHolder.holder;
    }
}
