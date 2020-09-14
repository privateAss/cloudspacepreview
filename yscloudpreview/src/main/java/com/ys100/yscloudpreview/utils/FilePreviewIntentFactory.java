package com.ys100.yscloudpreview.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.tencent.smtt.sdk.TbsVideo;
import com.ys100.yscloudpreview.R;
import com.ys100.yscloudpreview.listener.PlayVideoListener;
import com.ys100.yscloudpreview.manager.YsPreViewEngine;

import java.util.HashMap;

/**
 * =======================================
 *
 * @author: Created by dengjie
 * @Date: 2019/12/5 0005
 * @Edition：1.0
 * @Description:
 * @ModifyDescription ：
 * <p>
 * ========================================
 */
public class FilePreviewIntentFactory {

    public static void startAudio(String url, String title, Context context) {
        PlayVideoListener playVideoListener = YsPreViewEngine.getInstance().getPlayVideoListener();
        if(playVideoListener != null){
            playVideoListener.onPlayVideo(context,url,title);
        }else {
            Bundle extraData = new Bundle();
            extraData.putInt("screenMode", 102);
            extraData.putString("title", title);
            TbsVideo.openVideo(context, url, extraData);
        }
    }

    public static Animation animation(Context context) {
        Animation vAnimationDrawable = AnimationUtils.loadAnimation(context, R.anim.view_wait_progress_dialog);
        LinearInterpolator lin = new LinearInterpolator();
        vAnimationDrawable.setInterpolator(lin);
        return vAnimationDrawable;
    }

    public static void startAnimation(Animation animation, View view) {
        view.startAnimation(animation);
    }

    public static void stopAnimation(View view) {
        view.clearAnimation();
    }


}
