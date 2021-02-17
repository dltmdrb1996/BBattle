package com.example.BBATTLE.database;

public class ExerciseRoutine {
    public Integer reps;
    public Integer set;
    public Integer total;
    public String name;

    public ExerciseRoutine() {}

    public ExerciseRoutine(Integer reps, Integer set, Integer total, String name) {
        this.reps = reps;
        this.set = set;
        this.total = total;
        this.name = name;
    }
}
