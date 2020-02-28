package com.ys100.yscloudpreview.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;

import java.util.List;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/24
 * Description:
 */
public class CommonUtils {
    public static boolean isAPPBroughtToBackground(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getClassName().equals(context.getClass().getName())) {//前台
                return true;
            }
        }
        return false;
    }

    //打正式包的时候一定记住要修改
    public static String getAbsolutePath(Context context){
        if(context == null)return "";
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
