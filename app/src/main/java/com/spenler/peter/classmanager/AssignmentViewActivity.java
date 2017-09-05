package com.spenler.peter.classmanager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Gaming PC on 2017-09-05.
 */

public class AssignmentViewActivity extends AppCompatActivity{
    public Activity activity;
    public View view;
    private Assignment assignment;

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
    }
}
