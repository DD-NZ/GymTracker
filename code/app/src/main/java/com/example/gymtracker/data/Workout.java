package com.example.gymtracker.data;

import java.io.Serializable;
import java.util.ArrayList;

//Each workout represents a day at the gym and contains a list of exerciese for that day.
public class Workout implements Serializable {

    private String Name;
    private ArrayList<Exercise> Exercises = new ArrayList<>();

    public Workout(String name) {
        Name = name;
    }
    public String getName() {
        return Name;
    }

    public ArrayList<Exercise> getExercises() {
        return Exercises;
    }

    public void addExercise(Exercise e){
        Exercises.add(e);
    }

    public void removeExercise(Exercise e){
        Exercises.remove(e);
    }

}
