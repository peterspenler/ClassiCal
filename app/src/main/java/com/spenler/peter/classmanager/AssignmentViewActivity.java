package com.spenler.peter.classmanager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Gaming PC on 2017-09-05.
 */

public class AssignmentViewActivity extends AppCompatActivity{
    public Activity activity;
    public View view;
    private Assignment assignment;

    SimpleDateFormat sdf;
    SimpleDateFormat stf;

    private TextView assignmentViewName;
    private TextView assignmentViewDueDate;
    private TextView assignmentViewDueIn;
    private TextView assignmentViewWorth;
    private TextView assignmentViewMarkTitle;
    private TextView assignmentViewMark;
    private ImageView assignmentViewCheckImage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);
        activity = this;
        assignment = CoreManager.currentAssignment;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(assignment.getName());
        actionBar.setBackgroundDrawable(new ColorDrawable(assignment.getColor()));
        int darkColor = CoreManager.darkenColor(assignment.getColor(), 0.7f);
        activity.getWindow().setStatusBarColor(darkColor);

        assignmentViewName = (TextView)findViewById(R.id.assignmentViewName);
        assignmentViewDueDate = (TextView)findViewById(R.id.assignmentViewDueDate);
        assignmentViewDueIn = (TextView)findViewById(R.id.assignmentViewDueIn);
        assignmentViewWorth = (TextView)findViewById(R.id.assignmentViewWorth);
        assignmentViewMarkTitle = (TextView)findViewById(R.id.assignmentViewMarkTitle);
        assignmentViewMark = (TextView)findViewById(R.id.assignmentViewMark);
        assignmentViewCheckImage = (ImageView)findViewById(R.id.assignmentViewCheckImage);

        sdf = new SimpleDateFormat("EEE MMM d", Locale.US);
        stf = new SimpleDateFormat("h:mm a", Locale.US);

        assignmentViewName.setText("For " + assignment.getCourseName());
        assignmentViewDueDate.setText("Due: " + sdf.format(assignment.getDueDate()) + " at " + stf.format(assignment.getDueDate()));
        assignmentViewDueIn.setText("Due in 4 days");
        assignmentViewWorth.setText(String.valueOf(assignment.getWeight()) + "%");
        if(assignment.isFinished())
            assignmentViewMarkTitle.setText("Mark");
        else
            assignmentViewMarkTitle.setText("Predicted Mark");
        assignmentViewMark.setText(assignment.getMarkString());
        if(assignment.isFinished())
            assignmentViewCheckImage.setVisibility(View.VISIBLE);
        else
            assignmentViewCheckImage.setVisibility(View.INVISIBLE);
    }
}
