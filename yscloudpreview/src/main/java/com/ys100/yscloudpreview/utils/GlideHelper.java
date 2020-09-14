package com.ys100.yscloudpreview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.ys100.yscloudpreview.R;

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
public class GlideHelper {
    public static void loadRoundImageByListener(Context context, String url, ImageView target, RequestListener<Bitmap> listener, boolean isFrist) {
        Glide.with(context).asBitmap().load(isFrist ? url : url).skipMemoryCache(true).disallowHardwareConfig()
                .fitCenter()
                .addListener(listener)
                .into(target);
    }

    public static void loadRoundVideoByListener(Context context, String url, ImageView target, RequestListener<Bitmap> listener, boolean isFrist) {
        Glide.with(context).asBitmap().load(isFrist ? url : url).skipMemoryCache(true).disallowHardwareConfig()
                .fitCenter()
                .error(R.mipmap.img_video)
                .addListener(listener)
                .into(target);
    }

    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        if (TextUtils.isEmpty(videoUrl)) return null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
}
