package com.unique.java.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
封装公共工具方法，如加载配置文件、json序列化等
 */
public class CommUtils {
    private  static final Gson GSON = new GsonBuilder().create();//创建Gson对象
    public static Properties loadProperties(String fileName){
        Properties p = new Properties();
        InputStream in = CommUtils.class.getClassLoader().getResourceAsStream(fileName);
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
    //将任意对象序列化为json字符串
    public static String object2Json(Object obj){
        return GSON.toJson(obj);
    }
    //反序列化,将任意json字符串反序列化为对象
    public static Object json2object(String jsonStr ,Class objClass){
        return GSON.fromJson(jsonStr,objClass);
    }
}
