package com.unique.java.client.service;

import com.unique.java.util.CommUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

public class Connect2Server {
    private static final String IP;
    private static final int PORT;

    private Socket client;
    private InputStream in;
    private OutputStream out;

    static {
        Properties pro = CommUtils.loadProperties("socket.properties");
        IP = pro.getProperty("ip");
        PORT = Integer.parseInt(pro.getProperty("port"));
    }
    public Connect2Server(){
        try {
            client = new Socket(IP,PORT);
            in = client.getInputStream();
            out = client.getOutputStream();
        } catch (IOException e) {
            System.out.println("与服务器建立连接失败");
            e.printStackTrace();
        }
    }
    public InputStream getIn() {
        return in;
    }
    public OutputStream getOut() {
        return out;
    }
}
