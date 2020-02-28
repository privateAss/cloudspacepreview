package com.ys100.yscloudpreview.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/2/14
 * Description:
 */
public class PreviewCacheBean implements Parcelable {
    private String fileName;

    private String uuid;

    private String url="";

    private String cachePath;

    public PreviewCacheBean() {
    }

    public PreviewCacheBean(String fileName, String uuid, String url, String cachePath) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.url = url;
        this.cachePath = cachePath;
    }

    protected PreviewCacheBean(Parcel in) {
        fileName = in.readString();
        uuid = in.readString();
        url = in.readString();
        cachePath = in.readString();
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public static final Creator<PreviewCacheBean> CREATOR = new Creator<PreviewCacheBean>() {
        @Override
        public PreviewCacheBean createFromParcel(Parcel in) {
            return new PreviewCacheBean(in);
        }

        @Override
        public PreviewCacheBean[] newArray(int size) {
            return new PreviewCacheBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(uuid);
        dest.writeString(url);
        dest.writeString(cachePath);
    }
}
