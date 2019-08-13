package com.unique.java.server;

import com.unique.java.util.CommUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    private static final String IP;
    private static final int PORT;
    private static Map<String,Socket> clientstMap = new ConcurrentHashMap<>();



    static {
        Properties pro = CommUtils.loadProperties("socket.properties");
        IP = pro.getProperty("ip");
        PORT = Integer.parseInt(pro.getProperty("port"));
    }

    private static class ExecuteClient implements Runnable{
        private Socket client ;
        private InputStream in ;
        private OutputStream out;

        public ExecuteClient(Socket client){
            this.client = client;
        }
        @Override
        public void run() {
            while (true){

            }
        }
        private void sendUserLogin(String msg){
            //向所有在线用户发送用户上线信息
//            for (Map.Entry<String,Socket>:clientstMap.entrySet()) {
//                Socket socket = new Socket();
//
//            }
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executors = Executors.newFixedThreadPool(50);
        for (int i = 0;i < 50;i++){
            System.out.println("等待客户端连接");
            Socket client = serverSocket.accept();
            System.out.println("有新的连接，端口号为："+client.getPort());
            executors.submit(new ExecuteClient(client));
        }
    }
}
