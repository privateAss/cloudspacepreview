package com.ys100.yscloudpreview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.tencent.smtt.sdk.TbsReaderView;
import com.ys100.yscloudpreview.base.BaseYsActivity;
import com.ys100.yscloudpreview.bean.LocalFileBean;
import com.ys100.yscloudpreview.utils.FilePreviewIntentFactory;
import com.ys100.yscloudpreview.utils.GlideHelper;

import java.util.ArrayList;
import java.util.List;

public class LocalFilePreActivity extends BaseYsActivity implements View.OnClickListener, RequestListener<Bitmap>, TbsReaderView.ReaderCallback {
    private TextView tvNumber;
    private TextView tvTitle;
    private ImageView ivClose;
    //图片
    private PhotoView photoView;
    //doc文件
    private LinearLayout llReader;
    private TbsReaderView readerView;
    //视频及音频
    private FrameLayout flPlay;
    //视频音频缩略图
    private ImageView ivThumbnail;
    //视频音频播放按钮
    private ImageView ivPlay;
    //出错
    private LinearLayout llError;
    //加载中
    private ImageView ivLoading;
    //上一个/下一个
    private ImageView ivLeftNext, ivRightNext;
    //预览文件的全部集合
    private List<LocalFileBean> mList;
    //当前预览的文件
    private LocalFileBean crrFileBean;
    //当前的位置
    private int crrPosition;
    private String downloadDirOrUpDirStr;

    private Animation mAnimation;

    public final static String PAGE_CRR_POSITION = "page_crr_position";
    public final static String PAGE_DATA = "page_data";
    public final static String PAGE_DOWN_LOAD_DIR = "page_down_load_dir";

    public static void startActivity(Context context, String downloadDirOrUpDirStr, int crrPosition, ArrayList<LocalFileBean> fileBeans) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(PAGE_CRR_POSITION, crrPosition);
        mBundle.putString(PAGE_DOWN_LOAD_DIR, downloadDirOrUpDirStr);
        mBundle.putParcelableArrayList(PAGE_DATA, fileBeans);
        Intent intent = new Intent(context, LocalFilePreActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    @Override
    protected int getResLayoutId() {
        return R.layout.activity_local_file_pre;
    }

    @Override
    protected void initViews() {
        tvNumber = findViewById(R.id.tv_number);
        tvTitle = findViewById(R.id.tv_title);
        ivClose = findViewById(R.id.iv_close);
        photoView = findViewById(R.id.photo_view);
        llReader = findViewById(R.id.ll_reader);
        flPlay = findViewById(R.id.fl_play);
        ivThumbnail = findViewById(R.id.iv_thumbnail);
        llError = findViewById(R.id.ll_error);
        ivPlay = findViewById(R.id.iv_play);
        ivLoading = findViewById(R.id.iv_loading);
        ivLeftNext = findViewById(R.id.iv_left_next);
        ivRightNext = findViewById(R.id.iv_right_next);
    }

    @Override
    protected void initLister() {
        ivClose.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivLeftNext.setOnClickListener(this);
        ivRightNext.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mAnimation = FilePreviewIntentFactory.animation(this);
        mList = getIntent().getParcelableArrayListExtra(PAGE_DATA);
        downloadDirOrUpDirStr = getIntent().getStringExtra(PAGE_DOWN_LOAD_DIR);
        crrPosition = getIntent().getIntExtra(PAGE_CRR_POSITION, 0);
        if (mList != null && !mList.isEmpty()) {
            crrFileBean = mList.get(crrPosition);
            setTitleAndNumber(crrFileBean, crrPosition);
            previewControl(crrFileBean);
        } else {
            onLoadingError();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == ivClose.getId()) {
            finish();
        } else if (id == ivPlay.getId()) {//视频播放
            if (crrFileBean != null)
                FilePreviewIntentFactory.startAudio(crrFileBean.getFilePath(), crrFileBean.getFileName(), this);
        } else if (id == ivLeftNext.getId()) {//上一个
            if (crrPosition == 0) return;
            crrPosition -= 1;
            crrFileBean = mList.get(crrPosition);
            setTitleAndNumber(crrFileBean, crrPosition);
            previewControl(crrFileBean);
        } else if (id == ivRightNext.getId()) {//下一个
            if (crrPosition == mList.size()-1) return;
            crrPosition += 1;
            crrFileBean = mList.get(crrPosition);
            setTitleAndNumber(crrFileBean, crrPosition);
            previewControl(crrFileBean);
        }
    }

    //设置title和数字
    private void setTitleAndNumber(LocalFileBean bean, int position) {
        tvNumber.setText(String.format(getResources().getString(R.string.preview_number), position + 1, mList.size()));
        tvTitle.setText(bean.getFileName());
        ivLeftNext.setImageResource(R.mipmap.ic_arrowl_p);
        ivRightNext.setImageResource(R.mipmap.ic_arrowr_p);
        ivLeftNext.setEnabled(true);
        ivRightNext.setEnabled(true);
        if (bean.isLastLeft(position)) {
            ivLeftNext.setImageResource(R.mipmap.ic_arrowl_n);
            ivLeftNext.setEnabled(false);
        }
        if (bean.isLastRight(position, mList.size())) {
            ivRightNext.setImageResource(R.mipmap.ic_arrowr_n);
            ivRightNext.setEnabled(false);
        }
    }

    //设置预览View控制
    private void previewControl(LocalFileBean bean) {
        setAllGone();
        //文件获取成功处理
        if (bean.isPic()) {//图片
            stopLoading();
            photoView.setVisibility(View.VISIBLE);
            GlideHelper.loadRoundImageByListener(this, bean.getFilePath(), photoView, this, false);
        } else if (bean.isDoc()) {//文件
            llReader.setVisibility(View.VISIBLE);
            readerView = new TbsReaderView(this, this);
            llReader.addView(readerView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            Bundle mBundle = new Bundle();
            mBundle.putString("filePath", bean.getFilePath());
            mBundle.putString("name", bean.getFileName());
            mBundle.putString("tempPath", downloadDirOrUpDirStr);
            boolean flag = readerView.preOpen(bean.parseFormat(), false);
            if (flag) {
                readerView.openFile(mBundle);
            } else {
                onLoadingError();
            }
        } else if (bean.isAudio() || bean.isVideo()) {//视频及音频
            flPlay.setVisibility(View.VISIBLE);
            stopLoading();
            GlideHelper.loadRoundVideoByListener(this, bean.getFilePath(), ivThumbnail, this, true);
        } else {
            onLoadingError();
        }
    }

    private void setAllGone() {
        photoView.setVisibility(View.GONE);
        llReader.setVisibility(View.GONE);
        flPlay.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
        ivLoading.setVisibility(View.GONE);
        if (readerView != null) {
            readerView.onStop();
            llReader.removeAllViews();
            readerView = null;
        }

    }

    private void onLoadingError() {
        stopLoading();
        //文件获取失败处理
        llError.setVisibility(View.VISIBLE);
    }


    public void stopLoading() {
        ivLoading.setVisibility(View.GONE);
        if (mAnimation != null) {
            FilePreviewIntentFactory.stopAnimation(ivLoading);
        }
    }

    public void startLoading() {
        ivLoading.setVisibility(View.VISIBLE);
        if (mAnimation != null) {
            FilePreviewIntentFactory.startAnimation(mAnimation, ivLoading);
        }
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readerView != null)
            readerView.onStop();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
}