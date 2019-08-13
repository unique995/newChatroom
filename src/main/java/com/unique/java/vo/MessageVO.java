package com.unique.java.vo;

/*
服务器与客户端传递信息载体
 */
public class MessageVO {
    /*
    告知服务器要做的动作
    1.注册
    2.私聊
     */
    private String type;
    //具体内容
    private String content;
    //私聊时
    private String to;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
