package com.ys100.yscloudpreview.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;
import com.ys100.yscloudpreview.utils.CommonUtils;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class PerDataManager implements SpHelper {
    private SpHelper spHelper;
    private MMKV previewCacheMMKV;

    private PerDataManager() {
        spHelper = new PreferenceIml();
        initMMKV();
    }

    public void initSp(SharedPreferences sharedPreferences) {
        if (spHelper != null) {
            return;
        }
        initSharedPreferences(sharedPreferences);
    }

    @Override
    public void initMMKV() {
        spHelper.initMMKV();
    }

    @Override
    public MMKV getMMKV() {
        return spHelper.getMMKV();
    }

    @Override
    public void initSharedPreferences(SharedPreferences sharedPreferences) {
        spHelper.initSharedPreferences(sharedPreferences);
    }

    public MMKV getPreviewCacheMMKV(Context context,String baseIp){
        if(previewCacheMMKV == null){
            previewCacheMMKV = MMKV.mmkvWithID("preview_cache_id", CommonUtils.getAbsolutePath(context) + "/YunKongJian/navtivepreview/" + baseIp);
        }

        return previewCacheMMKV;
    }

    private static class DataHolder {
        private static PerDataManager holder = new PerDataManager();
    }

    public static PerDataManager getInstance() {
        return DataHolder.holder;
    }


}
