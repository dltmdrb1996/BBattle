package com.example.BBATTLE.database;

public class GuestInfo {
    public String email;
    public String nickname;
    public Integer age;
    public Double weight;
    public Double height;
    public Double bmi;
    public Integer total_point;
    public Integer current_point;
    public String gender;
    public Integer flag;
    public String battle;

    public GuestInfo() {}

    public GuestInfo(String email, String nickname, Integer age, Double weight, Double height, Double bmi, Integer total_point, Integer current_point, String gender, Integer flag, String battle) {
        this.email = email;
        this.nickname = nickname;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.total_point = total_point;
        this.current_point = current_point;
        this.gender = gender;
        this.flag = flag;
        this.battle = battle;
    }
}
