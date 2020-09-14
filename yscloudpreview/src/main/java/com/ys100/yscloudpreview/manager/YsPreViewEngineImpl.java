package com.ys100.yscloudpreview.manager;

import com.ys100.yscloudpreview.listener.ActivityAddOrRemoveListener;
import com.ys100.yscloudpreview.listener.PlayVideoListener;
import com.ys100.yscloudpreview.listener.SendEventToHtml5Listener;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/3/20
 * Description:
 */
public class YsPreViewEngineImpl extends BaseYsPreViewEngine {
    private ActivityAddOrRemoveListener activityAddOrRemoveListener;

    private SendEventToHtml5Listener html5Listener;

    private PlayVideoListener playVideoListener;
    @Override
    public void bindPageAddOrRemove(ActivityAddOrRemoveListener listener) {
        this.activityAddOrRemoveListener = listener;
    }

    @Override
    public ActivityAddOrRemoveListener getAddOrRemoveListener() {
        return activityAddOrRemoveListener;
    }


    @Override
    public void bindSendEventToHtml5(SendEventToHtml5Listener listener) {
        this.html5Listener = listener;
    }

    @Override
    public SendEventToHtml5Listener getHtml5Listener() {
        return html5Listener;
    }

    @Override
    public void playVideoListener(PlayVideoListener videoListener) {
        this.playVideoListener = videoListener;
    }

    @Override
    public PlayVideoListener getPlayVideoListener() {
        return playVideoListener;
    }


}
