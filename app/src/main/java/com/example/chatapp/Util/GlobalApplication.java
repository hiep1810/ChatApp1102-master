package com.example.chatapp.Util;

import android.app.Application;

import java.util.List;

public class GlobalApplication extends Application {
    private String userId;
    private String username;
    private List<String> lstFriend;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getLstFriend() {
        return lstFriend;
    }

    public void setLstFriend(List<String> lstFriend) {
        this.lstFriend = lstFriend;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
