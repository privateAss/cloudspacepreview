package com.ys100.cloudspacepreview;

import android.app.Application;

import com.uzmap.pkg.openapi.APICloud;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/21
 * Description:
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        APICloud.initialize(this);
    }
}
