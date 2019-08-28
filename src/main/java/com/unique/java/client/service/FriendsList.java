package com.unique.java.client.service;

import com.unique.java.util.CommUtils;
import com.unique.java.vo.MessageVO;

import javax.swing.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FriendsList {
    private JScrollPane friendsList;
    private JButton GroupButton;
    private JPanel FriendsList;
    private JScrollPane groupListPanel;
    private JFrame frame;

    private String userName;
    //存储所有在线好友
    private Set<String> users;
    //存储所有群名称以及群好友
    private Map<String,Set<String>> groupList = new ConcurrentHashMap<>();
    private Connect2Server connect2Server;
    //私聊
    private Map<String,PrivateChatGUI> PaivateChatGUIList = new ConcurrentHashMap<String,PrivateChatGUI>();
    //群聊
    private Map<String,GroupChatGUI> groupChatGUIList = new ConcurrentHashMap<>();

    //好友列表后台任务，不断监听服务器发来的信息
    //好友上线信息，用户私聊、群聊
    private class DaemonTask implements Runnable{
        private Scanner in = new Scanner(connect2Server.getIn());
        @Override
        public void run() {
            while (true){
                //收到服务器发来的消息
                if (in.hasNextLine()){
                    String strFromServer = in.nextLine();
                    //此时服务器发来的是一个json字符串
                    if (strFromServer.startsWith("{")){
                        MessageVO messageVO = (MessageVO) CommUtils.json2object(strFromServer,MessageVO.class);
                        if (messageVO.getType().equals("2")){
                            //服务器发来的私聊消息
                            String friendName = messageVO.getContent().split("-")[0];
                            String msg = messageVO.getContent().split("-")[1];
                            //判断此私聊是否是第一次创建
                            if (PaivateChatGUIList.containsKey(friendName)){
                                PrivateChatGUI privateChatGUI = PaivateChatGUIList.get(friendName);
                                privateChatGUI.getFrame().setVisible(true);
                                privateChatGUI.readFromServer(friendName+"说"+msg);
                            }else {
                                PrivateChatGUI privateChatGUI = new PrivateChatGUI(friendName,userName,connect2Server);
                                PaivateChatGUIList.put(friendName,privateChatGUI);
                                privateChatGUI.readFromServer(friendName+"说"+msg);
                            }
                        }else if(messageVO.getType().equals("4")){
                            //收到服务器发来的群聊消息
                            //type：4
                            //content:sender-msg
                            //to:groupName-[1,2,3...]
                            String groupName = messageVO.getTo().split("-")[0];
                            String senderName = messageVO.getContent().split("-")[0];
                            String groupMsg = messageVO.getContent().split("-")[1];
                            //若此群名称在群聊列表
                            if (groupList.containsKey(groupName)){
                                if (groupChatGUIList.containsKey(groupName)){
                                    //群聊界面弹出
                                    GroupChatGUI groupChatGUI = groupChatGUIList.get(groupName);
                                    groupChatGUI.getFrame().setVisible(true);
                                    groupChatGUI.readFromServer(senderName+"说"+groupMsg);

                                }else {
                                    Set<String> names = groupList.get(groupName);
                                    GroupChatGUI groupChatGUI = new GroupChatGUI(groupName,names,userName,connect2Server);
                                    groupChatGUIList.put(groupName,groupChatGUI);
                                    groupChatGUI.readFromServer(senderName+"说："+groupMsg);

                                }
                            }else {
                                //若群成员第一次收到群聊消息
                                //1.将群名称以及群成员保存到当前客户端群聊列表
                                Set<String> friends = (Set<String>) CommUtils.json2object(messageVO.getTo().split("-")[1],Set.class);
                                groupList.put(groupName,friends);
                                loadGroupList();
                                //2.弹出群聊界面
                                GroupChatGUI groupChatGUI = new GroupChatGUI(groupName,friends,userName,connect2Server);
                                groupChatGUIList.put(groupName,groupChatGUI);
                                groupChatGUI.readFromServer(senderName+"说："+groupMsg);
                            }
                        }
                    }else {
                        //newLogin:userName
                        if (strFromServer.startsWith("newLogin:")){
                            String newFriendName = strFromServer.split(":")[1];
                            users.add(newFriendName);
                            //弹框提示用户上线
                            JOptionPane.showMessageDialog(frame,newFriendName+"上线了","上线提醒",JOptionPane.INFORMATION_MESSAGE);
                            //刷新好友列表
                            loadUsers();
                        }
                    }
                }
            }
        }
    }

    //标签点击事件
    private class PrivateLableAction implements MouseListener{
        private String lableName;
        public PrivateLableAction(String lableName){
            this.lableName = lableName;
        }
        //鼠标点击执行事件
        @Override
        public void mouseClicked(MouseEvent e) {
            //判断好友列表私聊界面缓存是否已经有指定标签
            if (PaivateChatGUIList.containsKey(lableName)){
                PrivateChatGUI privateChatGUI = PaivateChatGUIList.get(lableName);
                privateChatGUI.getFrame().setVisible(true);
            }else {
                //第一次点击，创建私聊界面
                PrivateChatGUI privateChatGUI = new PrivateChatGUI(lableName,userName,connect2Server);
                PaivateChatGUIList.put(lableName,privateChatGUI);
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {

        }
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        @Override
        public void mouseEntered(MouseEvent e) {

        }
        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    //群聊点击事件
    private class GroupLableAction implements MouseListener{
        private String groupName;
        public GroupLableAction(String groupName){
            this.groupName = groupName;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (groupChatGUIList.containsKey(groupName)){
                GroupChatGUI groupChatGUI = groupChatGUIList.get(groupName);
                groupChatGUI.getFrame().setVisible(true);
            }else {
                Set<String> names = groupList.get(groupName);
                GroupChatGUI groupChatGUI = new GroupChatGUI(groupName,names,userName,connect2Server);
                groupChatGUIList.put(groupName,groupChatGUI);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    public FriendsList(String userName,Set<String> users,Connect2Server connect2Server){
        this.userName = userName;
        this.users = users;
        this.connect2Server = connect2Server;

        frame = new JFrame(userName);
        frame.setContentPane(FriendsList);
        //默认关闭窗口不做处理
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        loadUsers();
        //启动后台线程不断监听服务器发来的消息
        Thread daemonThread = new Thread(new DaemonTask());
        daemonThread.setDaemon(true);
        daemonThread.start();
        //创建群组
        GroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateGroupGUI(userName,users,connect2Server,FriendsList.this);
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null,"是否退出登录","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_NO_OPTION){
                    MessageVO messageVO = new MessageVO();
                    //退出登录
                    //type:0
                    //content:username
                    messageVO.setType("0");
                    messageVO.setContent(userName);
                    String json = CommUtils.object2Json(messageVO);
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(json);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
    }



    //加载所有用户在线信息
    public void loadUsers(){
        JLabel[] userLables = new JLabel[users.size()];
        JPanel friends = new JPanel();
        friends.setLayout(new BoxLayout(friends,BoxLayout.Y_AXIS));
        //set遍历
        Iterator<String> iterator = users.iterator();
        int i = 0;
        while (iterator.hasNext()){
            String userName = iterator.next();
            userLables[i] = new JLabel(userName);
        //添加标签点击事件
            userLables[i].addMouseListener(new PrivateLableAction(userName));
            friends.add(userLables[i]);
            i++;
        }
        friendsList.setViewportView(friends);
        //设置滚动条垂直滚动
        friendsList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        friends.revalidate();
        friendsList.revalidate();
    }
    public void loadGroupList(){
        //存储所有群名称标签JPanel
        JPanel groupNamePanel = new JPanel();
        groupNamePanel.setLayout(new BoxLayout(groupNamePanel,BoxLayout.Y_AXIS));
        JLabel[] lables = new JLabel[groupList.size()];
        //Map遍历
        Set<Map.Entry<String,Set<String>>> entries = groupList.entrySet();
        Iterator<Map.Entry<String,Set<String>>> iterator = entries.iterator();
        int i = 0;
        while (iterator.hasNext()){
            Map.Entry<String,Set<String>> entry = iterator.next();
            lables[i] = new JLabel(entry.getKey());
            lables[i].addMouseListener(new GroupLableAction(entry.getKey()));
            groupNamePanel.add(lables[i]);
            i++;
        }
        groupListPanel.setViewportView(groupNamePanel);
        groupListPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        groupNamePanel.revalidate();
    }
    public void addGroup(String groupName,Set<String> friends){
        groupList.put(groupName,friends);
    }
}
