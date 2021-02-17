package com.example.BBATTLE.board;

public class BoardRecyclerItem {
    String nickname, title, date, memo;

    public BoardRecyclerItem() {}

    public BoardRecyclerItem(String nickname, String title, String date, String memo) {
        this.nickname = nickname;
        this.title = title;
        this.date = date;
        this.memo = memo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }
}
