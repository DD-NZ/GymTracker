package com.example.gymtracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.data.Day;
import com.example.gymtracker.data.Set;
import com.example.gymtracker.data.Exercise;

import java.util.Timer;
import java.util.TimerTask;

//activity for adding sets to an exercise.
public class ExerciseActivity extends AppCompatActivity {

    //number of sets done for the day
    private int setCount = 1;
    //storing all the sets within the day
    private Day day = new Day();

    private String workoutName;
    private String exerciseName;
    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setLongClickListeners();
        workoutName = (String) getIntent().getSerializableExtra("workout");
        exerciseName = (String) getIntent().getSerializableExtra("exercise");
        exercise = Model.getExercise(workoutName,exerciseName);
        TextView l = (TextView) this.findViewById(R.id.workoutTitle3);
        l.setText(exercise.getName());
        initialisePrevious();
    }

    //Prints all of the data from the last time exercise was done.
    public void initialisePrevious(){

        Day previousDay = exercise.getLastDay();
        if(previousDay==null){
            TextView t = (TextView) this.findViewById(R.id.previousRep);
            t.append("\nYou havnt done this exercise before");
        }else{
            TextView set = (TextView) this.findViewById(R.id.previousSet);
            TextView rep = (TextView) this.findViewById(R.id.previousRep);
            TextView weight = (TextView) this.findViewById(R.id.previousWeight);
            TextView repText= (TextView) this.findViewById(R.id.repText);
            TextView weightText= (TextView) this.findViewById(R.id.weightText);
            set.append("Set");
            rep.append("Rep");
            weight.append("Weight");
            for(int i=1;i<previousDay.getSetNum();i++){
                Set s= previousDay.getSet(i);
                if(i==1){
                    repText.setText(""+s.rep);
                    weightText.setText(""+s.weight);
                }
                set.append("\n"+i);
                rep.append("\n"+s.rep);
                weight.append("\n"+s.weight);
            }
        }
    }

    //Takes the user to the overall progress of the exerercise.
    public void progress_Click(View v){
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.putExtra("workout",workoutName);
        intent.putExtra("exercise",exerciseName);
        startActivity(intent);
    }

    //adds a set to the day
    public void enterSet_Click(View v){
        TextView setText = this.findViewById(R.id.setNumber);
        TextView rep = this.findViewById(R.id.repText);
        TextView weight = this.findViewById(R.id.weightText);
        day.addSet(Integer.parseInt(rep.getText().toString()),Double.parseDouble(weight.getText().toString()));
        setText.setText("Set : "+(++setCount));
        System.out.println(day.getSet(1).rep+" "+day.getSet(1).weight);
    }

    //adds the day to the exercise and returns to the workout activity.
    public void completeExercise_Click(View v){
        if(day.getSetNum()==1){
            Toast.makeText(this, "You havn't added any Sets!", Toast.LENGTH_SHORT).show();
            return;
        }
        exercise.addDay(day);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",exercise);
        setResult(Activity.RESULT_OK,returnIntent);
        save();
        this.finish();
    }

    public void back_Click(View v){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        this.finish();
    }

    //for incrementing the number of reps done
    public void repUp_Click(View v){
        TextView repText = this.findViewById(R.id.repText);
        Integer num = Integer.parseInt(repText.getText().toString());
        repText.setText((++num).toString());
    }

    //for decrementing the number of reps done.
    public void repDown_Click(View v){
        TextView repText = this.findViewById(R.id.repText);
        Integer num = Integer.parseInt(repText.getText().toString());
        if(--num<0){return;}
        repText.setText((num).toString());
    }

    //incrementing the amount of weight
    public void weightUp_Click(View v){
        TextView weightText = this.findViewById(R.id.weightText);
        Double num = Double.parseDouble(weightText.getText().toString());
        num+=0.5;
        weightText.setText(num.toString());
    }

    //decrementing the amount of weight
    public void weightDown_Click(View v){
        TextView weightText = this.findViewById(R.id.weightText);
        Double num = Double.parseDouble(weightText.getText().toString());
        num-=0.5;
        if(num<0){return;}
        weightText.setText(num.toString());
    }

    //long click listerent so when incrementing or decrementing weight the user canvas
    // hold the button down to quickly change the amount of weight.
    private void setLongClickListeners(){
        final ImageButton upWeight = this.findViewById(R.id.weightUp);
        final ImageButton downWeight = this.findViewById(R.id.weightDown);

        upWeight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        if(upWeight.isPressed()) {
                            weightUp_Click(upWeight);
                        }
                        else
                            timer.cancel();
                    }
                },50,100);

                return true;
            }
        });
        downWeight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        if(downWeight.isPressed()) {
                            weightDown_Click(upWeight);
                        }
                        else
                            timer.cancel();
                    }
                },50,100);

                return true;
            }
        });
    }

    private void save(){
        Model.saveData(this);
    }
}
