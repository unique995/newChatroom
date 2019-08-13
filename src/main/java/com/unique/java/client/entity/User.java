package com.unique.java.client.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
实体类
 */

public class User {
    private Integer id;
    private String username;
    private String password;
    private String brief;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
