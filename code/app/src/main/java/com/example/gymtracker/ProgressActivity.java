package com.example.gymtracker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymtracker.data.Day;
import com.example.gymtracker.data.Exercise;
import com.example.gymtracker.data.Set;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

//activity for showing the user their progress.
public class ProgressActivity extends AppCompatActivity {

    Exercise exercise;
    int displayDay=0;
    ExerciseGraph graph;
    int percentage = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        String workoutName = (String) getIntent().getSerializableExtra("workout");
        String exerciseName = (String) getIntent().getSerializableExtra("exercise");
        exercise = Model.getExercise(workoutName,exerciseName);


        // Random r = new Random();
        // this.exercise = new Exercise("TEST");
        // for(int i=0;i<20;i++){
        //     System.out.println("created");
        //     Day d = new Day();
        //     d.addSet(8,(r.nextDouble()*100));
        //     exercise.addDay(d);
        // }

        //seek bar for zooming in on the graph.
        SeekBar sk=(SeekBar) findViewById(R.id.seekBar);
        sk.setProgress(percentage);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                percentage=seekBar.getProgress();
                updateGraph();
            }
        });
        initialise();
    }

    //creates a  exercise graph to display.
    public void initialise(){
        LinearLayout l = (LinearLayout) this.findViewById(R.id.progressLayout);
        graph= new ExerciseGraph(this, exercise.getDays());
        l.addView(graph);
        displayDay();
    }

    //updates the graph when zooming or moving across days.
    private void updateGraph(){
        ArrayList<Day> days = exercise.getDays();
        ArrayList<Day> subset = new ArrayList<>();
        int number = days.size()*percentage/100;
        for(int i=days.size()-number;i<days.size();i++){
            subset.add(days.get(i));
        }
        displayDay = days.size()-number;
        graph.days = subset;
        graph.highlight=0;
        graph.invalidate();
        displayDay();
    }

    //displays data from the highlighted day.
    public void displayDay(){
        TextView l = (TextView) this.findViewById(R.id.dayInfo);
        try {
            Day d = exercise.getDays().get(displayDay);
            l.setText("");
            l.append(d.getFormattedDate() + "\n");
            for (int i = 1; i < d.getSetNum(); i++) {
                Set set = d.getSet(i);
                l.append("Set " + i + ": " + set.rep + " reps, " + Math.round(set.weight) + "kgs\n");
            }
        }catch (Exception e){
            l.setText("");
        }
    }

    //moves the highlighted day to the day to the left.
    public void left_Click(View v){
        if(displayDay==0){
            return;
        }
        else{
            displayDay--;
            graph.highlight--;
            displayDay();
            graph.invalidate();
        }
    }

    //moves the highlighted day to the day to the right.
    public void rightClick(View v){
        if(displayDay==exercise.getDays().size()-1){
            return;
        }
        else{
            displayDay++;
            graph.highlight++;
            displayDay();
            graph.invalidate();
        }
    }

    public void back_Click(View v){
        this.finish();
    }
}
