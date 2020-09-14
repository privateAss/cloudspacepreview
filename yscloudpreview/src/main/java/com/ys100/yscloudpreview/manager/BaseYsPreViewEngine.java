package com.ys100.yscloudpreview.manager;

import com.ys100.yscloudpreview.listener.ActivityAddOrRemoveListener;
import com.ys100.yscloudpreview.listener.PlayVideoListener;
import com.ys100.yscloudpreview.listener.SendEventToHtml5Listener;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/3/20
 * Description:预览View接口引擎
 */
public abstract class BaseYsPreViewEngine {

    //绑定预览页面（为了页面管理）
    public abstract void bindPageAddOrRemove(ActivityAddOrRemoveListener listener);

    public abstract ActivityAddOrRemoveListener getAddOrRemoveListener();

    //绑定原生发送到H5的事件
    public abstract void bindSendEventToHtml5(SendEventToHtml5Listener listener);

    public abstract SendEventToHtml5Listener getHtml5Listener();

    public abstract void playVideoListener(PlayVideoListener videoListener);

    public abstract PlayVideoListener getPlayVideoListener();

}
