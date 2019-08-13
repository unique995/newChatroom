package com.unique.java.util;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Properties;

import static org.junit.Assert.*;

public class CommUtilsTest {

    @Test
    public void loadProperties() {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        Assert.assertNotNull(properties);
    }
}