package com.unique.java.client.service;

import com.unique.java.util.CommUtils;
import com.unique.java.vo.MessageVO;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

public class GroupChatGUI {
    private JTextArea readFromServer;
    private JPanel friendsPanel;
    private JTextField sendToServer;
    private JPanel groupPanel;

    private String groupName;
    private Set<String> friends;
    private String myName;
    private Connect2Server connect2Server;
    private JFrame frame;
    public GroupChatGUI(String groupName,Set<String> friends,String myName,Connect2Server connect2Server){
        this.groupName = groupName;
        this.friends = friends;
        this.myName = myName;
        this.connect2Server = connect2Server;

        frame = new JFrame(groupName);
        frame.setContentPane(groupPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,300);
        frame.setVisible(true);
        //加载好友列表
        friendsPanel.setLayout(new BoxLayout(friendsPanel,BoxLayout.Y_AXIS));
        Iterator<String> iterator = friends.iterator();
        while (iterator.hasNext()){
            String labelName = iterator.next();
            JLabel jLabel = new JLabel(labelName);
            friendsPanel.add(jLabel);

        }
        sendToServer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append(sendToServer.getText());
                //捕捉回车时间
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    String strToServer = sb.toString();
                    //type:4
                    //content:myName-msg
                    //to:groupName
                    MessageVO messageVO = new MessageVO();
                    messageVO.setType("4");
                    messageVO.setContent(myName+"-"+strToServer);
                    messageVO.setTo(groupName);
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(CommUtils.object2Json(messageVO));
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public JFrame getFrame() {
        return frame;
    }
    public void readFromServer(String msg){
        readFromServer.append(msg+"\n");
    }
}
