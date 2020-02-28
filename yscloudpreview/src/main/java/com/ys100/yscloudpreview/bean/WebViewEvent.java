package com.ys100.yscloudpreview.bean;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class WebViewEvent {

    public static final String EVENT_INITPREVIEW = "CallNativePreviewInit";
    public static final String EVENT_CHANGENATIVEPREVIEWPAGE = "changeNativePreviewPage";
    public static final String EVENT_RESPONSEPREVIEWURL = "CallNativePreviewChangePage";
    public static final String EVENT_CALLNATIVEPREVIEWSHOWERROR = "CallNativePreviewShowError";
    public static final String EVENT_WRITEPREVIEWCACHEURL = "writePreviewCacheUrl";
    public static final String EVENT_READPREVIEWCACHEURL = "readPreviewCacheUrl";
    public static final String EVENT_READPREVIEWCACHEURLCOMPLETE = "readPreviewCacheUrlComplete";
}
