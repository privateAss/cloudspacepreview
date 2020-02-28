package com.ys100.yscloudpreview.data;

import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;

/**
 * @author Created by wulei
 * @date 2019-06-25
 * @description
 */
public interface SpHelper {

    void initMMKV();

    MMKV getMMKV();


    void initSharedPreferences(SharedPreferences sharedPreferences);

}
