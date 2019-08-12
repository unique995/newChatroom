package com.unique.java.client.dao;

import com.unique.java.client.entity.User;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountDaoTest {
    AccountDao accountDao = new AccountDao();
    @Test
    public void userReg() {
        User user = new User();
        user.setUsername("WMM");
        user.setPassword("123456");
        user.setBrief("beauty");
        boolean flag = accountDao.userReg(user);
        Assert.assertTrue(flag);
    }

    @Test
    public void userLogin() {
        User user = new User();
        String username = "test1";
        String password = "123";
        user = accountDao.userLogin(username,password);
        Assert.assertNotNull(user);
    }
}