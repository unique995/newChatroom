package com.unique.java.server;

import com.unique.java.util.CommUtils;
import com.unique.java.vo.MessageVO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    private static final String IP;
    private static final int PORT;
    //缓存当前服务器所有在线的客户端信息
    private static Map<String,Socket> clientstMap = new ConcurrentHashMap<>();
    //缓存当前服务器注册的所有群名称以及群好友
    private static Map<String,Set<String>> groups = new ConcurrentHashMap<>();
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
                    }else if(msgFromClient.getType().equals("2")){
                        //用户私聊
                        //type:2
                        //content:myName-msg
                        //to:friendName
                        String friendName = msgFromClient.getTo();
                        Socket clientSocket = clientstMap.get(friendName);
                        try {
                            PrintStream out = new PrintStream(clientSocket.getOutputStream(),true,"UTF-8");
                            MessageVO msgToClient = new MessageVO();
                            msgToClient.setType("2");
                            msgToClient.setContent(msgFromClient.getContent());
                            System.out.println("收到私聊信息，内容为"+msgFromClient.getContent());
                            out.println(CommUtils.object2Json(msgToClient));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (msgFromClient.getType().equals("3")){
                        //注册群
                        String groupName = msgFromClient.getContent();
                        //该群的所有群成员
                        Set<String> friends = (Set<String>) CommUtils.json2object(msgFromClient.getTo(),Set.class);
                        groups.put(groupName,friends);
                        System.out.println("有新的群注册成功，群名称为"+groupName+",一共有"+groups.size()+"个群");
                    }else if (msgFromClient.getType().equals("4")){
                        //群聊信息
                        System.out.println("服务器收到的群聊消息为："+msgFromClient);
                        String groupName = msgFromClient.getContent();
                        Set<String> names = groups.get(groupName);
                        Iterator<String> iterator = names.iterator();
                        while (iterator.hasNext()){
                            String socketName = iterator.next();
                            Socket client = clientstMap.get(socketName);
                            try {
                                PrintStream out = new PrintStream(client.getOutputStream(),true,"UTF-8");
                                MessageVO messageVO = new MessageVO();
                                messageVO.setType("4");
                                messageVO.setContent(msgFromClient.getContent());
                                //群名-[]
                                messageVO.setTo(groupName+"-"+CommUtils.object2Json(names));
                                out.println(CommUtils.object2Json(messageVO));
                                System.out.println("服务器端发来的群聊消息为："+messageVO);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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
