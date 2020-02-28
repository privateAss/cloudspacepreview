package com.ys100.yscloudpreview.manager;

import com.uzmap.pkg.openapi.ExternalActivity;
import com.uzmap.pkg.openapi.Html5EventListener;
import com.uzmap.pkg.openapi.SuperWebview;
import com.uzmap.pkg.openapi.WebViewProvider;
import com.ys100.yscloudpreview.listener.Html5ReceiveListener;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class SendOrReceiveEventManager {
    private Object object;

    private Html5ReceiveListener receiveListener;

    public SendOrReceiveEventManager(Object object, Html5ReceiveListener receiveListener) {
        this.object = object;
        this.receiveListener = receiveListener;
    }

    public SendOrReceiveEventManager addH5Listener(String... events) {
        if (object == null || receiveListener == null || events.length == 0) return this;
        for (String event : events) {
            if (object instanceof SuperWebview) {
                ((SuperWebview) object).addHtml5EventListener(new Html5EventListener(event) {
                    @Override
                    public void onReceive(WebViewProvider webViewProvider, Object o) {
                        receiveListener.onReceive(event, o);
                    }
                });
            } else if (object instanceof ExternalActivity) {
                ((ExternalActivity) object).addHtml5EventListener(new Html5EventListener(event) {
                    @Override
                    public void onReceive(WebViewProvider webViewProvider, Object o) {
                        receiveListener.onReceive(event, o);
                    }
                });
            }
        }

        return this;
    }
}
