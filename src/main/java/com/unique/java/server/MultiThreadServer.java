package com.unique.java.server;

import com.unique.java.util.CommUtils;
import com.unique.java.vo.MessageVO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
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
        private Scanner in ;
        private PrintStream out;

        public ExecuteClient(Socket client){
            this.client = client;
            try {
                this.in = new Scanner(client.getInputStream());
                this.out = new PrintStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            while (true){
                if (in.hasNextLine()){
                    String jsonStrFromClient = in.nextLine();
                    MessageVO msgFromClient = (MessageVO) CommUtils.json2object(jsonStrFromClient,MessageVO.class);
                    // 新用户注册到服务端
                    if (msgFromClient.getType().equals("1")){
                        String userName = msgFromClient.getContent();
                        // 将当前在线的所有用户名发回客户端
                        MessageVO msg2Client = new MessageVO();
                        msg2Client.setType("1");
                        msg2Client.setContent(CommUtils.object2Json(clientstMap.keySet()));
                        out.println(CommUtils.object2Json(msg2Client));
                        // 将新上线的用户信息发回给当前已在线的所有用户
                        sendUserLogin("newLogin:"+userName);
                        // 将当前新用户注册到服务端缓存
                        clientstMap.put(userName,client);
                        System.out.println(userName+"上线了!");
                        System.out.println("当前聊天室共有"+ clientstMap.size()+"人");
                    }
                }
            }
        }
        private void sendUserLogin(String msg){
            //向所有在线用户发送用户上线信息
            for (Map.Entry<String,Socket> entry :clientstMap.entrySet()) {
                Socket socket = entry.getValue();
                try {
                    PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
                    out.println(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
