package com.example.ditebattle.database;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String password;
    private String name;
    private String nickname;
    private Integer age;
    private String phone;
    private Double weight;
    private Double height;
    private Double bmi;
    private Integer total_point;
    private Integer current_point;
    private String gender;

    public User(String id, String password, String name, String nickname, Integer age, String phone, Double weight ,Double height,
                Double bmi, Integer total_point, Integer current_point, String gender){
        this.id= id;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.age = age;
        this.phone = phone;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.total_point = total_point;
        this.current_point = current_point;
        this.gender = gender;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("password", password);
        result.put("name",name);
        result.put("nickname",nickname);
        result.put("age",age);
        result.put("phone",phone);
        result.put("weight",weight);
        result.put("height",height);
        result.put("bmi", bmi);
        result.put("total_point",total_point);
        result.put("current_point",current_point);
        result.put("gender",gender);
        return result;
    }
}
