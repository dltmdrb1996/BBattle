package com.example.BBATTLE.database;

public class Chating {
    String userName, message;

    public Chating() {
        super();
    }

    public Chating(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
}
