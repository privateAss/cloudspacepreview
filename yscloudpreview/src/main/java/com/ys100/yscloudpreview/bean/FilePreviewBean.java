package com.ys100.yscloudpreview.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * =======================================
 *
 * @author: Created by dengjie
 * @Date: 2019/12/4 0004
 * @Edition：1.0
 * @Description:
 * @ModifyDescription ：
 * <p>
 * ========================================
 */
public class FilePreviewBean implements Parcelable {

    public final static String SUCCESS = "success";
    public final static String UNSUCCESS = "error";
    /**
     * cursor : 2
     * uuid : cf4db60a-7593-8242-7af3-0777fbc3a1e5
     * name : 诗一样的风景.jpg
     * url :
     * status :
     * type : image
     * msg :
     * total : 8
     */

    private int currIndex;
    private String uuid;
    private String name;
    private String url;
    private String status;
    private String fileType;
    private String error;
    private int total;

    protected FilePreviewBean(Parcel in) {
        currIndex = in.readInt();
        uuid = in.readString();
        name = in.readString();
        url = in.readString();
        status = in.readString();
        fileType = in.readString();
        error = in.readString();
        total = in.readInt();
    }

    public static final Creator<FilePreviewBean> CREATOR = new Creator<FilePreviewBean>() {
        @Override
        public FilePreviewBean createFromParcel(Parcel in) {
            return new FilePreviewBean(in);
        }

        @Override
        public FilePreviewBean[] newArray(int size) {
            return new FilePreviewBean[size];
        }
    };

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isLastLeft(int mCurrentCurson) {
        if (mCurrentCurson <= 1) {
            return true;
        }
        return false;
    }

    public boolean isLastRight(int mCurrentCurson, int total) {
        if (mCurrentCurson == total) {
            return true;
        }
        return false;
    }


    public boolean isImage() {
        if (TextUtils.isEmpty(this.fileType)) return false;
        return this.fileType.toLowerCase().equals("image");
    }

    public boolean isDoc() {
        if (TextUtils.isEmpty(this.fileType)) return false;
        return this.fileType.toLowerCase().equals("document") || this.fileType.toLowerCase().equals("ysnote");
    }

    public boolean isVideoOrAudio() {
        if (TextUtils.isEmpty(this.fileType)) return false;
        return this.fileType.toLowerCase().equals("video") || this.fileType.toLowerCase().equals("music");
    }

    public boolean isSuccess() {
        if (TextUtils.isEmpty(this.status)) return false;
        return this.status.equals(SUCCESS);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currIndex);
        dest.writeString(uuid);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(status);
        dest.writeString(fileType);
        dest.writeString(error);
        dest.writeInt(total);
    }
}
