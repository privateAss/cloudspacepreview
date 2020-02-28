package com.ys100.yscloudpreview.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.tencent.mmkv.MMKV;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class PreferenceIml implements SpHelper {
    private SharedPreferences mySp;

    private MMKV mmkv;

    @Override
    public void initMMKV() {
        mmkv = MMKV.defaultMMKV();
    }

    @Override
    public MMKV getMMKV() {
        if (mmkv != null) {
            return mmkv;
        } else {
            return null;
        }
    }

    @Override
    public void initSharedPreferences(SharedPreferences sharedPreferences) {
        if (sharedPreferences == null) {
            Log.e("PreferenceIml", "SharedPreferences is null!!!");
        }
        if (mySp != null) {
            return;
        }
        mySp = sharedPreferences;
    }
}
