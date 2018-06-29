package com.example.zhangwb.model;

import java.io.Serializable;

public class User implements Serializable{
    private String userName;
    private String password;
    private String userId;
    private String nickName;
    private String teachingTerm;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTeachingTerm() {
        return teachingTerm;
    }

    public void setTeachingTerm(String teachingTerm) {
        this.teachingTerm = teachingTerm;
    }
}
