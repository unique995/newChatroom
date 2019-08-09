package com.unique.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
封装公共工具方法，如加载配置文件、json序列化等
 */
public class CommUtils {
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
}
