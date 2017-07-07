package com.spenler.peter.classmanager;

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

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.md_green_700));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*//
        CoreManager.addCourse("Chemistry", 0.5, R.color.md_amber_800);
        CoreManager.addCourse("Anthropology", 0.5, R.color.md_blue_600);
        CoreManager.addCourse("Math", 0.5, R.color.md_amber_800);
        CoreManager.addCourse("MicroBiology", 0.5, R.color.md_blue_600);
        CoreManager.addCourse("Calculus I", 0.5, R.color.md_amber_800);
        CoreManager.addCourse("Computer Organization", 0.5, R.color.md_blue_600);
        CoreManager.addCourse("Programming II", 0.5, R.color.md_amber_800);
        CoreManager.addCourse("Engineering Design", 0.5, R.color.md_blue_600);
        CoreManager.addCourse("Computer Organization", 0.5, R.color.md_blue_600);
        CoreManager.addCourse("Programming II", 0.5, R.color.md_amber_800);
        CoreManager.addCourse("Engineering Design", 0.5, R.color.md_blue_600);
        //*/
    }

    private void setupViewPager (ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CoursesFragment(), "Courses");
        adapter.addFragment(new Tab2Fragment(), "Assignments");
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
        final AddCourseDialog addCourseDialog = new AddCourseDialog();

        addCourseDialog.show(getFragmentManager(), "Add Course Dialog");
    }
}
