package com.ys100.yscloudpreview.manager;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/3/20
 * Description:在线预览引擎
 */
public class YsPreViewEngine {

    private YsPreViewEngine() {
    }

    private static class Holder {
        private static BaseYsPreViewEngine sEngine = new YsPreViewEngineImpl();
    }

    public static BaseYsPreViewEngine getInstance() {
        return Holder.sEngine;
    }
}
