package com.ys100.yscloudpreview.data;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.ys100.yscloudpreview.bean.PreviewCacheBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/14
 * Description:
 */
public class PreviewCacheManager {

    private MMKV cacheMMkv;

    public static final String PREVIEW_CACHE_KEY = "preview_cache";

    private static final String PAGE_NAME_KEY = "page_name";

    private static class PreviewCacheHolder {
        private static PreviewCacheManager holder = new PreviewCacheManager();
    }

    public static PreviewCacheManager getInstance() {
        return PreviewCacheHolder.holder;
    }

    public PreviewCacheManager setMMKV(Context context,String baseIp) {
        if (cacheMMkv == null)
            cacheMMkv = PerDataManager.getInstance().getPreviewCacheMMKV(context,baseIp);
        return this;
    }

    public PreviewCacheManager setPreviewCache(PreviewCacheBean bean) {
        if (cacheMMkv == null||bean==null)
            return this;
        List<PreviewCacheBean> previewCache = getPreviewCache();
        if(previewCache == null){
            previewCache = new ArrayList<>();
        }
        if(previewCache.size()==0){
            previewCache.add(bean);
        }else {
            boolean hasCache = false;
            for (PreviewCacheBean cacheBean :previewCache) {
                if(!TextUtils.isEmpty(cacheBean.getUuid())&&bean.getUuid().equals(cacheBean.getUuid())){
                    hasCache = true;
                    break;
                }
            }
            if(!hasCache){
                previewCache.add(bean);
            }
        }

        setPreviewCache(previewCache);

        return this;
    }

    private PreviewCacheManager setPreviewCache(List<PreviewCacheBean> mLists) {
        if (cacheMMkv == null)
            return this;
        cacheMMkv.encode(PREVIEW_CACHE_KEY, new Gson().toJson(mLists));
        return this;
    }


    private List<PreviewCacheBean> getPreviewCache(){
        if(cacheMMkv == null)return null;
        String s = cacheMMkv.decodeString(PREVIEW_CACHE_KEY);
        if(TextUtils.isEmpty(s))return new ArrayList<>();
        return new Gson().fromJson(s, new TypeToken<List<PreviewCacheBean>>() {}.getType());
    }

    public PreviewCacheBean getPreViewCachByUUID(String uuid){
        PreviewCacheBean cacheBean = null;
        if(TextUtils.isEmpty(uuid))return null;
        List<PreviewCacheBean> previewCache = getPreviewCache();
        if(previewCache==null)return null;
        for (PreviewCacheBean bean :previewCache) {
            if(!TextUtils.isEmpty(bean.getUuid())&&bean.getUuid().equals(uuid)){
                cacheBean = bean;
                break;
            }
        }
        return cacheBean;
    }

    public PreviewCacheManager setPageName(String pageName){
        if (cacheMMkv == null)
            return this;
        cacheMMkv.encode(PAGE_NAME_KEY,pageName);
        return this;
    }

    public String  getPageName(){
        if(cacheMMkv == null)return null;
        return cacheMMkv.decodeString(PAGE_NAME_KEY,"");
    }
}
