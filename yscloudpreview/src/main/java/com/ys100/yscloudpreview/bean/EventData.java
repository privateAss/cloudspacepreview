package com.ys100.yscloudpreview.bean;

import org.json.JSONObject;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/3/20
 * Description:
 */
public class EventData {
    //事件名
    private String eventName;
    //事件的类容
    private JSONObject object;

    public EventData() {

    }

    public EventData(String eventName, JSONObject object) {
        this.eventName = eventName;
        this.object = object;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
