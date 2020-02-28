package com.ys100.yscloudpreview.listener;

import android.app.Activity;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public interface ActivityAddOrRemoveListener {
    void onAddActivity(Activity activity);

    void onRemoveActivity(Activity activity);
}
