package com.example.gymtracker.data;

import java.io.Serializable;

// Set contains the weight lifted for a number of reps
public class Set implements Serializable {
    public final Integer rep;
    public final Double weight;

    public Set(Integer rep, Double weight) {
        this.rep = rep;
        this.weight = weight;
    }
}
