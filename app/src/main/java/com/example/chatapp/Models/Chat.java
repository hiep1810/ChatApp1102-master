package com.example.chatapp.Models;

import java.util.List;

public class Chat {
    private String _id;
    private String chat_name;
    private String newest_mess;
    private List<ChatContent> content;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getNewest_mess() {
        return newest_mess;
    }

    public void setNewest_mess(String newest_mess) {
        this.newest_mess = newest_mess;
    }

    public List<ChatContent> getContent() {
        return content;
    }

    public void setContent(List<ChatContent> content) {
        this.content = content;
    }
}
