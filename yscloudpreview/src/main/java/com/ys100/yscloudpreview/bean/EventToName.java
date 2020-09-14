package com.ys100.yscloudpreview.bean;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class EventToName {
    //预览H5发送原生的事件
    public static final String EVENT_CALLNATIVEPREVIEWINIT = "CallNativePreviewInit";
    public static final String EVENT_WRITEPREVIEWCACHEURL = "writePreviewCacheUrl";
    public static final String EVENT_READPREVIEWCACHEURL = "readPreviewCacheUrl";
    public static final String EVENT_CALLNATIVEPREVIEWCHANGEPAGE = "CallNativePreviewChangePage";
    public static final String EVENT_CALLNATIVEPREVIEWSHOWERROR = "CallNativePreviewShowError";

    //预览原生发送到H5的事件
    public static final String EVENT_CHANGENATIVEPREVIEWPAGE = "changeNativePreviewPage";
    public static final String EVENT_READPREVIEWCACHEURLCOMPLETE = "readPreviewCacheUrlComplete";
}
