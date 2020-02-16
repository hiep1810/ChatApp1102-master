package com.example.chatapp.Models;

public class ChatContent {
    private String timestamp;
    private String sendUser;
    private String chat;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public ChatContent(String timestamp, String sendUser, String chat) {
        this.timestamp = timestamp;
        this.sendUser = sendUser;
        this.chat = chat;
    }
}
