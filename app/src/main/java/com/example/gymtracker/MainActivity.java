package com.example.gymtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gymtracker.data.Workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Main homescreen Displays all of the saved workouts.
public class MainActivity extends AppCompatActivity {

    //Stores if app is creating a workout.
    private boolean creatingWorkout = false;
    //Stores if app is deleting workouts.
    private boolean deleteMode = false;
    //List of all the saved workouts.
    public static ArrayList<Workout> workouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Loads the workouts from storage and creates a button for each workout
        Model.load(this);
        workouts = Model.getWorkouts();
        for(int i=0; i<workouts.size();i++){
            addButton(true, workouts.get(i).getName());
        }
    }

    //drop down bar to create a new workout
    public void newWorkout_Click(View v){
        LinearLayout l = (LinearLayout) this.findViewById(R.id.enterWorkout);
        if(creatingWorkout){
            l.removeView(this.findViewById(R.id.sendWorkout));
            creatingWorkout=false;
            return;
        }
        creatingWorkout=true;

        LinearLayout textEntry = new LinearLayout(this);
        textEntry.setBackgroundColor(getColor(R.color.DarkGrey));
        textEntry.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textParams.weight = 1;
        textParams.setMargins(15,15,15,15);

        EditText  e= new EditText(this);
        e.setId(R.id.workoutInputText);
        e.setLayoutParams(textParams);
        e.setBackgroundColor(getColor(R.color.White));

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 6;

        ImageButton b = new ImageButton(this);
        b.setLayoutParams(buttonParams);
        b.setImageDrawable(getDrawable(R.drawable.send));
        b.setColorFilter(getColor(R.color.Orange));
        b.setBackgroundColor(getColor(R.color.DarkGrey));
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout c = ((LinearLayout)v.getParent());
                LinearLayout cc = ((LinearLayout) c.getParent());
                String text = ((EditText)c.findViewById(R.id.workoutInputText)).getText().toString();
                if(!text.equals("")){
                    //checks inputted text is not already a workout name.
                    if(!checkOriginal(text)){
                        return;
                    }
                    //creates a new workout and adds a button for it
                    workouts.add(new Workout(text));
                    addButton(false,text);
                    cc.removeView(c);
                    //saves data with new workout.
                    save();
                }
            }
        });

        textEntry.addView(e);
        textEntry.addView(b);
        textEntry.setId(R.id.sendWorkout);
        l.addView(textEntry);

    }

    //gets DP for screen scaling
    private int getDP(int i){
        float scale = this.getResources().getDisplayMetrics().density;
        return (int)(i*scale+0.5f);
    }

    //adds a button to the list of workout buttons
    public void addButton(boolean created,String s){

        LinearLayout l = (LinearLayout) this.findViewById(R.id.workoutScroll);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        Button b =new Button(this);
        b.setLayoutParams(buttonParams);
        b.setText(s);
        b.setBackground(getDrawable(R.drawable.roundbutton));
        b.setTextColor(getColor(R.color.Orange));
        b.setTextSize(24);
        b.setAllCaps(false);
        //click listener for going to the exercises for the workout
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWorkoutActivity(((Button)v).getText().toString());
            }
        });

        //long click listener for deleting workouts.
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                deleteOption(v);
                return true;
            }
        });

        int margin = getDP(15);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearParams.weight = 1;
        linearParams.setMargins(margin,margin,margin,margin);
        linearParams.height=getDP(75);
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setLayoutParams(linearParams);

        inner.addView(b);
        l.addView(inner);
    }

    //Checks if workout name is orginal.
    public boolean checkOriginal(String s){
        for(Workout w: workouts){
            if(w.getName().equals(s)){
                Toast.makeText(this, "You already have this Workout!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    //goes to the specific workout activity to display the exercises.
    public void goToWorkoutActivity(String s){
        Intent intent = new Intent(this, WorkoutActivity.class);
        Workout workout=null;
        for(Workout w: workouts){
            if(w.getName().equals(s)){
                workout=w;
                if(w.getExercises().size()>0){
                }
            }
        }
        intent.putExtra("Workout", workout.getName());
        startActivityForResult(intent,1);
    }

    // for deleting buttons from the screen when deleteing workouts.
    private void deleteOption(View v){
        LinearLayout l =(LinearLayout) v.getParent().getParent();
        if(deleteMode){
            for(int i = 0; i < l.getChildCount(); i++){
                LinearLayout ll =  (LinearLayout) l.getChildAt(i);
                View view = ll.getChildAt(1);
                ll.removeView(view);
            }
            deleteMode = false;
        }else{
            for(int i = 0; i < l.getChildCount(); i++){
                LinearLayout ll =  (LinearLayout) l.getChildAt(i);
                ll.addView(createDeleteButton());
            }
            deleteMode = true;
        }
    }

    //adds all the delete buttons when entering delete mode.
    private ImageButton createDeleteButton(){
        LinearLayout.LayoutParams b2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        b2Params.weight = 4;
        int margin = getDP(15);
        b2Params.setMargins(margin,margin,margin,margin);
        ImageButton b = new ImageButton(this);
        Drawable icon=this.getResources().getDrawable( R.drawable.clear);
        b.setImageDrawable(icon);
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.setBackground(getDrawable(R.drawable.deletebutton));
        b.setLayoutParams(b2Params);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout l = (LinearLayout) v.getParent().getParent();
                String workoutName = ((Button)((LinearLayout)v.getParent()).getChildAt(0)).getText().toString();
                Model.removeWorkout(workoutName);
                l.removeView((View)v.getParent());
                save();
            }
        });
        return b;
    }


    //saving the data to the model.
    private void save(){
        Model.saveData(this);
    }
}
