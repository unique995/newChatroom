package com.unique.java.client.service;

import com.unique.java.client.dao.AccountDao;
import com.unique.java.client.entity.User;
import com.unique.java.util.CommUtils;
import com.unique.java.vo.MessageVO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Set;


public class userLogin {
    private JTextField userNametext;
    private JButton regButton;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JLabel QQ;
    private JLabel userName;
    private JLabel password;
    private JPanel Reg;
    private AccountDao accountDao = new AccountDao();

    public userLogin(){
        JFrame frame = new JFrame("用户登录");
        frame.setContentPane(Reg);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //注册按钮
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserReg();
            }
        });
        //登录按钮
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //校验用户信息
                String userName = userNametext.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = accountDao.userLogin(userName,password);
                if (user != null){
                    //成功
                    JOptionPane.showMessageDialog(frame,"登录成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);//关闭提示框
                    //加载用户列表
                    //与服务器建立连接，将当前用户的用户名和密码发到服务器
                    Connect2Server connect2Server = new Connect2Server();

                    MessageVO msg2Server = new MessageVO();
                    msg2Server.setType("1");
                    msg2Server.setContent(userName);
                    String json2Server = CommUtils.object2Json(msg2Server);
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(json2Server);
                        // 读取服务端发回的所有在线用户信息
                        Scanner in = new Scanner(connect2Server.getIn());
                        if (in.hasNextLine()){
                            String msgFromServerStr = in.nextLine();
                            MessageVO msgFromServer = (MessageVO) CommUtils.json2object(msgFromServerStr,MessageVO.class);
                            Set<String> users = (Set<String>) CommUtils.json2object(msgFromServer.getContent(),Set.class);
                            System.out.println("所有在线用户为:"+users);
                            // 加载用户列表界面
                            // 将当前用户名、所有在线好友、与服务器建立连接传递到好友列表界面
                            new FriendsList(userName,users,connect2Server);
                        }

                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    //失败，停留在当前登录页面，提示信息错误
                    JOptionPane.showMessageDialog(frame,"登录失败","提示信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        userLogin user = new userLogin();
    }
}
