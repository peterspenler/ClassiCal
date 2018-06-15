package com.spenler.peter.classmanager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spenler.peter.classmanager.dialogs.AddAssignmentDialog;
import com.spenler.peter.classmanager.dialogs.AddCourseDialog;
import com.spenler.peter.classmanager.fragments.AssignmentFragment;
import com.spenler.peter.classmanager.core.CoreManager;
import com.spenler.peter.classmanager.fragments.CoursesFragment;
import com.spenler.peter.classmanager.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private CoursesFragment coursesFragment;
    private static AssignmentFragment assignmentFragment;
    public Context context;
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        activity = this;

        Log.d("SAVE DEBUGGING", "OnCreate called");
        CoreManager.loadData();

        coursesFragment = new CoursesFragment();
        assignmentFragment = new AssignmentFragment();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.md_green_700));

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager (ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(coursesFragment, "Courses");
        adapter.addFragment(assignmentFragment, "Assignments");
        viewPager.setAdapter(adapter);
    }

    public void openCalendar(View view){
        Log.i("MainActivity", "Calendar Button Pressed");
    }

    private class SectionsPageAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        SectionsPageAdapter(FragmentManager fm) {
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

    public void addItem(View view){
        int page = mViewPager.getCurrentItem();
        if(page == 0){
            final AddCourseDialog addCourseDialog = new AddCourseDialog();
            addCourseDialog.show(getFragmentManager(), "Add Course Dialog");
        }
        if(page == 1){
            final AddAssignmentDialog addAssignmentDialog = new AddAssignmentDialog();
            CoreManager.setCurrentCourse(CoreManager.getCourseByIndex(0));
            Bundle b = new Bundle();
            b.putInt("type", 1);
            addAssignmentDialog.setArguments(b);
            addAssignmentDialog.show(getFragmentManager(), "Add Assignment Dialog");
        }
    }

    public static AssignmentFragment getAssignmentFragment() {
        return assignmentFragment;
    }

    @Override
    public void onPause(){
        CoreManager.saveData();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        CoreManager.saveData();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        CoreManager.saveData();
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
        int currentPosition = mViewPager.getCurrentItem();
        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(currentPosition);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        int currentPosition = mViewPager.getCurrentItem();
        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(currentPosition);
    }
}
