package com.example.BBATTLE;

import java.util.HashMap;
import java.util.Map;

public class RecyclerItemData {
    String number, title, memo, userName, message;
    Boolean master;

    public RecyclerItemData() {
    }

    public RecyclerItemData(String number, String title, String memo, Boolean master) {
        this.number = number;
        this.title = title;
        this.memo = memo;
        this.master = master;
    }

    public RecyclerItemData(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }


    public Boolean getMaster() {
        return master;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getMemo() {
        return memo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumber(String number) { this.number = number; }

    public void setTitle(String title) { this.title = title; }

    public void setMemo(String memo) { this.memo = memo; }

    public void setMaster(Boolean master) { this.master = master; }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("number", number);
        result.put("title", title);
        result.put("memo", memo);
        result.put("userName", userName);
        result.put("message", message);
        result.put("master",master);
        return result;
    }
}
