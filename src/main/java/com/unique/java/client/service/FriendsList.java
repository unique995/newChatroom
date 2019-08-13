package com.unique.java.client.service;

import javax.swing.*;
import java.util.Iterator;
import java.util.Set;

public class FriendsList {
    private JScrollPane friendsList;
    private JButton GroupButton;
    private JPanel FriendsList;

    private JFrame frame;
    private String userName;
    private Set<String> users;
    private Connect2Server connect2Server;

    public FriendsList(String userName,Set<String> users,Connect2Server connect2Server){
        this.userName = userName;
        this.users = users;
        this.connect2Server = connect2Server;

        JFrame frame = new JFrame("FriendsList");
        frame.setContentPane(FriendsList);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        loadUsers();
    }

    //加载所有用户在线信息
    public void loadUsers(){
        JLabel[] userLables = new JLabel[users.size()];
        JPanel friends = new JPanel();
        friends.setLayout(new BoxLayout(friends,BoxLayout.Y_AXIS));
        //遍历
        Iterator<String> iterator = users.iterator();
        int i = 0;
        while (iterator.hasNext()){
            String userName = iterator.next();
            userLables[i] = new JLabel(userName);
            friends.add(userLables[i]);
            i++;
        }
        friendsList.setViewportView(friends);
        //设置滚动条垂直滚动
        friendsList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        friends.revalidate();
        friendsList.revalidate();
    }
}
