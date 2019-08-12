package com.unique.java.client.service;

import com.unique.java.client.dao.AccountDao;
import com.unique.java.client.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserReg {
    private JPanel userRegPanel;
    private JLabel userName;
    private JTextField userNametextField1;
    private JPasswordField passwordField1;
    private JTextField brieftextField1;
    private JButton 注册Button;
    private AccountDao accountDao = new AccountDao();

    public UserReg(){
        JFrame frame = new JFrame("用户注册");
        frame.setContentPane(userRegPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        //点击注册按钮，将信息持久化到db中，成功弹出提示框

        注册Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNametextField1.getText();
                String password = String.valueOf(passwordField1.getPassword());
                String brief = brieftextField1.getText();
                User user = new User();
                user.setUsername(userName);
                user.setPassword(password);
                user.setBrief(brief);
                //调用dao对象
                if (accountDao.userReg(user)){
                    //弹出提示框
                    JOptionPane.showMessageDialog(frame,"注册成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    //关闭提示框
                    frame.setVisible(false);
                }else
                    JOptionPane.showMessageDialog(frame,"注册失败","提示信息",JOptionPane.ERROR_MESSAGE);
            }
        });
    }


}
