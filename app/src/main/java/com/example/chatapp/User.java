package com.example.chatapp;

public class User {
    // 모델. comment 받아오기 위함
    String email;
    String text;

    // getter and setter 로 구현
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
