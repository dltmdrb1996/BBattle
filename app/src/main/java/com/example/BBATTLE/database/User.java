package com.example.BBATTLE.database;

import java.util.HashMap;
import java.util.Map;

public class User {
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

    public User() {

    }

    public User(String id, String nickname, Integer age, Double weight , Double height,
                Double bmi, Integer total_point, Integer current_point, String gender, Integer flag,String battle){
        this.email= id;
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

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("nickname",nickname);
        result.put("age",age);
        result.put("weight",weight);
        result.put("height",height);
        result.put("bmi", bmi);
        result.put("total_point",total_point);
        result.put("current_point",current_point);
        result.put("gender",gender);
        result.put("flag",flag);
        result.put("battle",battle);
        return result;
    }

}
