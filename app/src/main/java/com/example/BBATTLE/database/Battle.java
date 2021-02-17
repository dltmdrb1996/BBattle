package com.example.BBATTLE.database;

import java.util.HashMap;
import java.util.Map;

public class Battle {
    public String master;
    public String guest;
    public long finish_time;
    public Integer masterHP;
    public Integer guestHP;
    public String grade;
    public Integer masterDay;
    public Integer guestDay;

    public String mmission1;
    public String mmission2;
    public String mmission3;
    public String mmission4;
    public String mmission5;

    public String gmission1;
    public String gmission2;
    public String gmission3;
    public String gmission4;
    public String gmission5;

    public String win;
    public String masterExit;
    public String guestExit;
    Battle(){
    }

    public Battle(String master, String guest, long finish_time, Integer masterHP, Integer guestHP, String grade, Integer masterDay, Integer guestDay
                   , String mmission1 , String mmission2 , String mmission3 , String mmission4 , String mmission5
            , String gmission1 , String gmission2 , String gmission3 , String gmission4 , String gmission5 , String win , String masterExit , String guestExit) {
        this.master = master;
        this.guest = guest;
        this.finish_time = finish_time;
        this.masterHP = masterHP;
        this.guestHP = guestHP;
        this.grade = grade;
        this.masterDay = masterDay;
        this.guestDay = guestDay;

        this.mmission1 = mmission1;
        this.mmission2 = mmission2;
        this.mmission3 = mmission3;
        this.mmission4 = mmission4;
        this.mmission5 = mmission5;

        this.gmission1 = gmission1;
        this.gmission2 = gmission2;
        this.gmission3 = gmission3;
        this.gmission4 = gmission4;
        this.gmission5 = gmission5;

        this.win = win;
        this.masterExit = masterExit;
        this.guestExit = guestExit;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("master", master);
        result.put("guest",guest);
        result.put("finish_time",finish_time);
        result.put("masterHP",masterHP);
        result.put("guestHP",guestHP);
        result.put("grade",grade);
        result.put("masterDay",masterDay);
        result.put("guestDay",guestDay);

        result.put("mmission1",mmission1);
        result.put("mmission2",mmission2);
        result.put("mmission3",mmission3);
        result.put("mmission4",mmission4);
        result.put("mmission5",mmission5);

        result.put("gmission1",gmission1);
        result.put("gmission2",gmission2);
        result.put("gmission3",gmission3);
        result.put("gmission4",gmission4);
        result.put("gmission5",gmission5);

        result.put("win",win);
        result.put("masterExit",masterExit);
        result.put("guestExit",guestExit);
        return result;
    }
}
