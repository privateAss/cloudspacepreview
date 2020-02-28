package com.ys100.yscloudpreview.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by chenxiaowei on 2019/5/25.
 */

public class GsonHelper {
    public static String toString(Object o) {
        return new Gson().toJson(o);
    }

    public static <T> T toObject(String str, Class<T> t) {
        return new Gson().fromJson(str, t);
    }

    public static <T> List<T> toList(String str, TypeToken<List<T>> typeToken){
        return new Gson().fromJson(str, typeToken.getType());
    }
}
