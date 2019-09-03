package com.example.gymtracker;

import android.content.Context;

import com.example.gymtracker.data.Exercise;
import com.example.gymtracker.data.Workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


// A class to store all of the workout data.
public class Model {
    public static ArrayList<Workout> workouts = new ArrayList<Workout>();

    public static ArrayList<Workout> getWorkouts(){
        return workouts;
    }

    //gets all exercises from a particular workout
    public static ArrayList<Exercise> getExercises(String workout){
        for(Workout w: workouts ){
            if(w.getName().equals(workout)){
                return w.getExercises();
            }
        }
        return null;
    }

    //gets a specific exercise from a workout.
    public static Exercise getExercise(String workout, String exercsise){
        for(Workout w: workouts ){
            if(w.getName().equals(workout)){
                for(Exercise e: w.getExercises()){
                    if(e.getName().equals(exercsise)){
                        return e;
                    }
                }
            }
        }
        return null;
    }

    //removes a workout and all of the exercises within.
    public static void removeWorkout(String workout){
        for(Workout w: workouts ){
            if(w.getName().equals(workout)){
                workouts.remove(w);
                return;
            }
        }
    }

    //Loads the data from phone storage
    public static void load(Context c){
        try{
            FileInputStream fis = c.openFileInput("save.dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            workouts = (ArrayList<Workout>) is.readObject();
            is.close();
            fis.close();

        }   catch(Exception e){
            //creates a new empty file if one is not there
            File file = new File(c.getFilesDir(), "save.dat");
        }
    }

    //saves the updated workout data to storage.
    public static void saveData(Context c){
        try{
            System.out.println("Saving");
            File file = new File(c.getFilesDir(), "save.dat");
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = c.openFileOutput("save.dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(MainActivity.workouts);
            os.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
