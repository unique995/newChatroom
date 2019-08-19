package com.unique.java.client.service;

import com.unique.java.util.CommUtils;
import com.unique.java.vo.MessageVO;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CreateGroupGUI {
    private JPanel friendLablePanel;
    private JLabel groupName;
    private JButton comform;
    private JTextField groupNameText;
    private JPanel CreateGroupGUI;

    private String myName;
    private Set<String> friends;
    private Connect2Server connect2Server;
    private FriendsList friendsList;

    public CreateGroupGUI(String myName,Set<String> friends,Connect2Server connect2Server,FriendsList friendsList) {

        this.myName = myName;
        this.friends = friends;
        this.connect2Server = connect2Server;
        this.friends = friends;
        this.friendsList = friendsList;

        JFrame frame = new JFrame("创建群组");
        frame.setContentPane(CreateGroupGUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //将在线好友以CheckBox展示到界面中
        friendLablePanel.setLayout(new BoxLayout(friendLablePanel,BoxLayout.Y_AXIS));
       // JCheckBox[] checkBoxes = new JCheckBox[friends.size()];
        Iterator<String> iterator = friends.iterator();
       // int i = 0;
        while (iterator.hasNext()){
            String lableName = iterator.next();
            JCheckBox checkBox = new JCheckBox(lableName);
            friendLablePanel.add(checkBox);
        }
        //点击提交按钮提交信息到服务端
        comform.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //判断哪些好友选中加入群聊
                Set<String> selectedFriends = new HashSet<>();
                Component[] comps = friendLablePanel.getComponents();
                for (Component comp: comps){
                    JCheckBox checkBox = (JCheckBox) comp;
                    if (checkBox.isSelected()){
                        String lableName = checkBox.getText();
                        selectedFriends.add(lableName);
                    }
                }
                selectedFriends.add(myName);
                //获取群名输入框
                String groupName = groupNameText.getText();
                //将群名+选中好友信息发送到服务端
                //type：3
                //content：群名
                //to：[user1,user2,user3]
                MessageVO messageVO = new MessageVO();
                messageVO.setType("3");
                messageVO.setContent(groupName);
                messageVO.setTo(CommUtils.object2Json(selectedFriends));

                try {
                    PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                    out.println(CommUtils.object2Json(messageVO));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                //4.将当前创建群界面隐藏，刷新好友界面的群列表
                frame.setVisible(false);
                friendsList.addGroup(groupName,selectedFriends);
                friendsList.loadGroupList();
            }
        });
    }


}
