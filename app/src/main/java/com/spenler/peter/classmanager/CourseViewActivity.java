package com.spenler.peter.classmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 5/31/17.
 */

public class CourseViewActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
    public static Activity activity;
    public static View view;
    private static Course course;

    private static TextView currentMarkView;
    private static TextView predictedMarkView;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Bundle bundle = getIntent().getExtras();
        activity = this;
        course = CoreManager.currentCourse;

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(course.getName());
        actionBar.setBackgroundDrawable(new ColorDrawable(course.getColor()));

        int darkColor = CoreManager.darkenColor(course.getColor(), 0.7f);
        activity.getWindow().setStatusBarColor(darkColor);

        TabLayout tabBar =  (TabLayout) findViewById(R.id.tabs);
        AppBarLayout tabBarBackground = (AppBarLayout) findViewById(R.id.appbar);
        int lightColor = CoreManager.darkenColor(course.getColor(), 1.1f);
        tabBar.setBackgroundColor(lightColor);
        tabBarBackground.setBackgroundColor(lightColor);

        currentMarkView = (TextView)findViewById(R.id.courseCurrentMarkText);
        predictedMarkView = (TextView)findViewById(R.id.coursePredictedMarkText);

        refreshGrades();
    }

    void refreshGrades(){
        predictedMarkView.setText("Predicted Mark: " + CoreManager.round(course.getPredictedGrade(),2));
        currentMarkView.setText("Current Mark: " + CoreManager.round(course.getGrade(), 2));
    }

    public void addItem(View view){
        Bundle bundle = new Bundle();
        //bundle.putParcelable("course",course);
        //CoreManager.currentCourse = course;

        final AddAssignmentDialog addAssignmentDialog = new AddAssignmentDialog();
        addAssignmentDialog.setArguments(bundle);
        int page = mViewPager.getCurrentItem();

        if(page == 0){
            Log.d("CURRENT PAGE", "Assignments");
        }
        if(page == 1){
            Log.d("CURRENT PAGE", "Tests");
        }
        if(page == 2){
            Log.d("CURRENT PAGE", "Classes");
        }

        addAssignmentDialog.show(getFragmentManager(), "Add Assignment Dialog");
    }

    private void setupViewPager (ViewPager viewPager){

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

     //   CourseAssignmentsFragment assignments = CourseAssignmentsFragment.newInstance(course);
     //   CourseTestsFragment tests = new CourseTestsFragment();
     //   CourseClassesFragment classes = new CourseClassesFragment();

    //    assignments.setArguments(bundle);
      //  tests.setArguments(bundle);
       // classes.setArguments(bundle);

        adapter.addFragment(CourseAssignmentsFragment.newInstance(course), "Assignments");
        adapter.addFragment(new CourseTestsFragment(), "Tests");
        adapter.addFragment(new CourseClassesFragment(), "Classes");
        viewPager.setAdapter(adapter);
    }

    public void openCalendar(){
        Log.i("MainActivity", "Calendar Button Pressed");
    }

    private class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshGrades();
        setupViewPager(mViewPager);
        Log.d("SAVE DEBUGGING", "OnResume called");
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        int currentPosition = mViewPager.getCurrentItem();
        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(currentPosition);
    }
}
