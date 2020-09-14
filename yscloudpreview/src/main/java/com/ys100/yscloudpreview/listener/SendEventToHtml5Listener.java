package com.ys100.yscloudpreview.listener;

import org.json.JSONObject;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/3/20
 * Description:
 */
public interface SendEventToHtml5Listener {
    /**
     * 原生发送Native事件到Html5
     * @param eventName 事件名称
     * @param extra 携带参数
     */
    void sendEventToHtml5(String eventName, JSONObject extra);


}
