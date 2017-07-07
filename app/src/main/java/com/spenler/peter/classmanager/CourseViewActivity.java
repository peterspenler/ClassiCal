package com.spenler.peter.classmanager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by peter on 5/31/17.
 */

public class CourseViewActivity extends AppCompatActivity{
    public static Activity activity;
    public static View view;
    private static Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Bundle bundle = getIntent().getExtras();

        ActionBar actionBar = getSupportActionBar();
        RelativeLayout infoLayout = (RelativeLayout) findViewById(R.id.courseInfoLayout);

        activity = this;
        course = bundle.getParcelable("course");

        actionBar.setTitle(course.getName());
        actionBar.setBackgroundDrawable(new ColorDrawable(course.getColor()));

        int darkColor = CoreManager.darkenColor(course.getColor(), 0.7f);
        activity.getWindow().setStatusBarColor(CoreManager.darkenColor(course.getColor(), 0.7f));
        infoLayout.setBackgroundColor(CoreManager.darkenColor(course.getColor(), 0.85f));

    }

    public void addItem(View view){
        Bundle bundle = new Bundle();
        bundle.putParcelable("course",course);

        final AddAssignmentDialog addAssignmentDialog = new AddAssignmentDialog();
        addAssignmentDialog.setArguments(bundle);

        addAssignmentDialog.show(getFragmentManager(), "Add Course Dialog");
    }
}
