package com.example.chatapp;

import java.io.Serializable;

public class ChatRoom implements Serializable {
    private String id;
    private String name;

    public ChatRoom(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}