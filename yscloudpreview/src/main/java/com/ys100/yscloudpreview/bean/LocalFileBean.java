package com.ys100.yscloudpreview.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ys100.yscloudpreview.utils.FileUtils;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/7/13
 * Description:
 */
public class LocalFileBean implements Parcelable {
    private String fileId;
    private String filePath;
    private String fileName;

    public LocalFileBean() {
    }

    public LocalFileBean(String fileId, String filePath, String fileName) {
        this.fileId = fileId;
        this.filePath = filePath;
        this.fileName = fileName;
    }


    protected LocalFileBean(Parcel in) {
        filePath = in.readString();
        fileName = in.readString();
    }

    public static final Creator<LocalFileBean> CREATOR = new Creator<LocalFileBean>() {
        @Override
        public LocalFileBean createFromParcel(Parcel in) {
            return new LocalFileBean(in);
        }

        @Override
        public LocalFileBean[] newArray(int size) {
            return new LocalFileBean[size];
        }
    };

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeString(fileName);
    }

    public boolean isLastLeft(int mCurrentCurson) {
        if (mCurrentCurson <= 0) {
            return true;
        }
        return false;
    }

    public boolean isLastRight(int mCurrentCurson, int total) {
        if (mCurrentCurson == total-1) {
            return true;
        }
        return false;
    }

    public boolean isDoc() {
        return !TextUtils.isEmpty(filePath) && FileUtils.isDocument(filePath);
    }

    public boolean isPic() {
        return !TextUtils.isEmpty(filePath) && FileUtils.isPic(filePath);
    }

    public boolean isAudio(){
        return !TextUtils.isEmpty(filePath) && FileUtils.isAudio(filePath);
    }

    public boolean isVideo(){
        return !TextUtils.isEmpty(filePath) && FileUtils.isVideo(filePath);
    }

    public String parseFormat(){
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
}
