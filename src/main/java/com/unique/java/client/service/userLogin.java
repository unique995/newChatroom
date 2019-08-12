package com.unique.java.client.service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class userLogin {
    private JTextField userNametext;
    private JButton regButton;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JLabel QQ;
    private JLabel userName;
    private JLabel password;
    private JPanel Reg;


    public userLogin(){

        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserReg();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("用户登录");
        frame.setContentPane(new userLogin().Reg);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
