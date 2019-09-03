package com.example.gymtracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.data.Day;
import com.example.gymtracker.data.Exercise;
import com.example.gymtracker.data.Workout;

import java.util.ArrayList;

//Activity for displaying all the exercises for the workout.
public class WorkoutActivity extends AppCompatActivity {


    String workoutName;
    ArrayList<Exercise> exercises;
    private boolean creatingExercise = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
         TextView l = (TextView) this.findViewById(R.id.workoutTitle2);
         workoutName = (String) getIntent().getSerializableExtra("Workout");
         exercises= Model.getExercises(workoutName);
         l.setText(workoutName);
         initialiseExercises();
    }

    //adds buttons for all the exercises in the workout.
    public void initialiseExercises(){
        for(Exercise e : exercises){
            addButton(e.getName());
        }
    }

    // drop down bar for creating a new exercise.
    public void newExercsie_Click(View v){
        LinearLayout l = (LinearLayout) this.findViewById(R.id.enterExercise);
        if(creatingExercise){
            l.removeView(this.findViewById(R.id.sendExercise));
            creatingExercise =false;
            return;
        }
        creatingExercise =true;

        LinearLayout textEntry = new LinearLayout(this);
        textEntry.setBackgroundColor(getResources().getColor(R.color.DarkGrey));
        textEntry.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textParams.weight = 1;
        textParams.setMargins(15,15,15,15);

        EditText e= new EditText(this);
        e.setId(R.id.exerciseInputText);
        e.setLayoutParams(textParams);
        e.setBackgroundColor(getResources().getColor(R.color.White));

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 6;

        ImageButton b = new ImageButton(this);
        b.setLayoutParams(buttonParams);
        b.setImageDrawable(getResources().getDrawable(R.drawable.send));
        b.setColorFilter(getResources().getColor(R.color.Orange));
        b.setBackgroundColor(getResources().getColor(R.color.DarkGrey));
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout c = ((LinearLayout)v.getParent());
                LinearLayout cc = ((LinearLayout) c.getParent());

                String text = ((EditText)c.findViewById(R.id.exerciseInputText)).getText().toString();
                //checks that exercise to be created is not already there.
                if(!text.equals("")){
                    if(!checkOriginal(text)){
                        cc.removeView(c);
                        return;
                    }
                    exercises.add(new Exercise(text));
                    addButton(text);
                    cc.removeView(c);
                    save();
                }
            }
        });
        textEntry.addView(e);
        textEntry.addView(b);
        textEntry.setId(R.id.sendExercise);
        l.addView(textEntry);
    }

    //adds a button for an exercise to the screen.
    public void addButton(String s){
        LinearLayout l = (LinearLayout) this.findViewById(R.id.exerciseScroll);
        int margin = getDP(15);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(margin,margin,margin,margin);
        params.height=getDP(75);

        Button b =new Button(this);
        b.setLayoutParams(params);
        b.setText(s);
        b.setBackground(getResources().getDrawable(R.drawable.roundbutton));
        b.setTextColor(getResources().getColor(R.color.Orange));
        b.setTextSize(24);
        b.setAllCaps(false);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExerciseActivity(((Button)v).getText().toString());
            }
        });
        l.addView(b);
    }


    private int getDP(int i){
        float scale = this.getResources().getDisplayMetrics().density;
        return (int)(i*scale+0.5f);
    }

    public boolean checkOriginal(String s){
        for(Exercise e: exercises){
            if(e.getName().equals(s)){
                Toast.makeText(this, "You already have this Exercise!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void goToExerciseActivity(String exerciseName){
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("workout",workoutName);
        intent.putExtra("exercise", exerciseName);
        startActivity(intent);
    }

    private void save(){
        Model.saveData(this);
    }


    public void back_Click(View v){
        this.finish();
    }
}
