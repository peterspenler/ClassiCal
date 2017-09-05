package com.spenler.peter.classmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private CoursesFragment coursesFragment;
    private AssignmentFragment assignmentFragment;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        Log.d("SAVE DEBUGGING", "OnCreate called");
        CoreManager.loadData();

        coursesFragment = new CoursesFragment();
        assignmentFragment = new AssignmentFragment();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.md_green_700));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager (ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(coursesFragment, "Courses");
        adapter.addFragment(assignmentFragment, "Assignments");
        viewPager.setAdapter(adapter);
    }

    public void openCalendar(){
        Log.i("MainActivity", "Calendar Button Pressed");
    }

    private class SectionsPageAdapter extends FragmentPagerAdapter{

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

    public void addItem(View view){
        int page = mViewPager.getCurrentItem();
        if(page == 0){
            final AddCourseDialog addCourseDialog = new AddCourseDialog();
            addCourseDialog.show(getFragmentManager(), "Add Course Dialog");
        }
        if(page == 1){
            final AddAssignmentDialog addAssignmentDialog = new AddAssignmentDialog();
            CoreManager.currentCourse = CoreManager.courses.get(0);
            addAssignmentDialog.show(getFragmentManager(), "Add Assignment Dialog");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        CoreManager.saveData();
        Log.d("SAVE DEBUGGING", "OnPause called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        CoreManager.saveData();
        Log.d("SAVE DEBUGGING", "OnDestroy called");
    }

    @Override
    public void onResume(){
        super.onResume();
        setupViewPager(mViewPager);
        Log.d("SAVE DEBUGGING", "OnResume called");
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        setupViewPager(mViewPager);
    }
}
