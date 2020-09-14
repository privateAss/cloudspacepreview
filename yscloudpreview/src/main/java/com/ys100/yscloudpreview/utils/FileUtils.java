package com.ys100.yscloudpreview.utils;

/**
 * @author Created by wulei
 * @date 2019-07-09
 * @description
 */
public class FileUtils {

    public static String getDir(String path) {
        return path.substring(0, path.lastIndexOf('/'));
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static boolean isPic(String path) {
        return path.endsWith(".png")
                || path.endsWith(".jpeg")
                || path.endsWith(".jpg")
                || path.endsWith(".bmp")
                || path.endsWith(".gif");
    }

    public static boolean isAudio(String path) {
        return path.endsWith(".wav")
                || path.endsWith(".ogg")
                || path.endsWith(".mp3");
    }

    public static boolean isVideo(String path) {
        return path.endsWith(".avi")
                || path.endsWith(".mkv")
                || path.endsWith(".rmvb")
                || path.endsWith(".webm")
                || path.endsWith(".mp4")
                || path.endsWith(".mov")
                || path.endsWith(".mpeg")
                || path.endsWith(".flv")
                || path.endsWith(".mpg")
                || path.endsWith(".wmv");
    }

    public static boolean isDocument(String path) {
        return path.endsWith(".pdf")
                || path.endsWith(".doc")
                || path.endsWith(".xls")
                || path.endsWith(".txt")
                || path.endsWith(".ppt")
                || path.endsWith(".docx")
                || path.endsWith(".pptx")
                || path.endsWith(".xlsx");
    }
}
