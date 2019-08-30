package com.example.gymtracker.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/*
* Represents One exercise done on a day at the gym.
*/
public class Day implements Serializable {

    // count is the number of set done
    private int count=1;
    //each set of the exercise hashmap refers to the set done e.g sets.get(1)
    //will return the first set of the exercise for that day.
    private HashMap<Integer,Set> sets = new HashMap<>();
    //Date exercise was done
    public final Date date;

    public Day() {
        this.date = new Date();
    }

    public void addSet(Integer rep, Double kg){
        sets.put(count,new Set(rep,kg));
        count++;
    }
    public Set getSet(int i){
        return sets.get(i);
    }

    public int getSetNum(){
        return count;
    }

    // Gets the 1RM for the day, so that different weights and reps combinations can be compared for
    //the progress graph
    public double get1RM(){
        double total = 0;
        for(int i = 1;i<count;i++){
            Set s = sets.get(i);
            total += s.weight/getpercentage(s.rep)*100;
        }
        return total/count;
    }

    public String getFormattedDate(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        StringBuilder s = new StringBuilder();
        s.append(timeFormat.format(date)+"\n");
        s.append(dateFormat.format(date));
        return s.toString();
    }

    // 1 Rep max calculator
    private double getpercentage(int rep){
        switch(rep) {
            case 1:
                return 0.100;
            case 2:
                return 0.95;
            case 3:
                return 0.93;
            case 4:
                return 0.90;
            case 5:
                return 0.87;
            case 6:
                return 0.85;
            case 7:
                return 0.83;
            case 8:
                return 0.80;
            case 9:
                return 0.77;
            case 10:
                return 0.75;
            case 11:
                return 0.73;
            default:
                return 0.70;
        }
    }



}
