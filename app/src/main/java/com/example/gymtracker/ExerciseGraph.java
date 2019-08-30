package com.example.gymtracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import com.example.gymtracker.data.Day;
import com.example.gymtracker.data.Set;
import java.util.ArrayList;


//creates a SurfaceView which draws a graph
public class ExerciseGraph extends SurfaceView
{
    Paint paint = null;
    //day to highlight
    public int highlight = 0;
    public ArrayList<Day> days;

    public ExerciseGraph(Context context, ArrayList<Day> days){
        super(context);
        this.days = days;
        paint = new Paint();
        setWillNotDraw(false);
    }

    //draws all the days to a graph on the canvas.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        double max = getMax(days);
        int total =days.size();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.DarkGrey));
        canvas.drawRect(0,0,width,height,paint);
        paint.setColor(getResources().getColor(R.color.Orange));

        int initialX = 40;
        int initialY = getHeight()-40;
        int totalX = getWidth()-80;
        int totalY = getHeight()-80;

        float prevX =0;
        float prevY =0;
        for(int i=0;i<days.size();i++){

            Day d = days.get(i);
            Set s1 = d.getSet(1);
            float x = (float)(initialX+(totalX*(i/(double)days.size())));
            float y = (float)(initialY-(totalY*(d.get1RM()/max)));
            if(i!=0){
                paint.setColor(Color.YELLOW);
                canvas.drawLine(prevX,prevY,x,y,paint);
            }
            if(i==highlight){
                paint.setColor(Color.GREEN);
            }else{
                paint.setColor(getResources().getColor(R.color.Orange));
            }
            canvas.drawCircle(x,y,15,paint);

            prevX=x;
            prevY=y;
        }
    }


    //gets the max day to help with scaling.
    private double getMax(ArrayList<Day> days){
        double max =0;
        for(Day d: days){
            double rm1 = d.get1RM();
            if(rm1>max){
                max = rm1;
            }
        }
        return max;
    }
}
