package com.example.gymtracker.data;

import java.io.Serializable;
import java.util.ArrayList;

//An exercise records each time you do the exercise as a "Day" and stores the largest lift done for the exercise
public class Exercise implements Serializable {

    private String name;
    private ArrayList<Day> days = new ArrayList<>();
    private double max = 0;

    public Exercise(String name) {
        this.name = name;
    }

    public void addDay(Day d){
        days.add(d);

        if(d.get1RM()>max){
            max = d.get1RM();
        }
    }
    public double getMax(){
        return  max;
    }

    //gets the day you did this exercise.
    public Day getLastDay(){
        if(days.isEmpty()){
            System.out.println("NO DAYS");
            return null;
        }
        System.out.println("DAYS");
        return days.get(days.size()-1);
    }


    public ArrayList<Day> getDays() {
        return days;
    }

    public int getNumdays(){
        return days.size();
    }

    public String getName(){
        return name;
    }
}
