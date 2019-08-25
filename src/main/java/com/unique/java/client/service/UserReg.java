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

    public static void IsEXist(String userName){

    }
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
                //判断注册的账户的密码是否符合注册要求
                //正则表达式：密码必须是数字+字母的组合并且长度是8-15
                String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,11}$";
                if (userName.length() >= 3 && !userName.contains(" ") && password.matches(reg)){
                    User user = new User();
                    user.setUsername(userName);
                    user.setPassword(password);
                    user.setBrief(brief);

                    if (accountDao.IsExist(userName)){
                        JOptionPane.showMessageDialog(frame,"用户已存在","提示信息",JOptionPane.ERROR_MESSAGE);
                    }else {
                        boolean flag = accountDao.userReg(user);
                        if (flag){
                            JOptionPane.showMessageDialog(frame,"注册成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
                            //关闭提示框
                            frame.setVisible(false);
                        }else {
                            JOptionPane.showMessageDialog(frame,"注册失败","提示信息",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(frame,"用户名长度必须大于等于3且不能出现空格，密码必须是包含字母、数字的6-11位字符串","提示信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


}
