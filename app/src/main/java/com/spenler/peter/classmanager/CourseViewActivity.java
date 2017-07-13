package com.spenler.peter.classmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 5/31/17.
 */

public class CourseViewActivity extends AppCompatActivity{
    public static Activity activity;
    public static View view;
    private static Course course;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Bundle bundle = getIntent().getExtras();
        activity = this;
        course = bundle.getParcelable("course");

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


    }

    public void addItem(View view){
        Bundle bundle = new Bundle();
        bundle.putParcelable("course",course);

        final AddAssignmentDialog addAssignmentDialog = new AddAssignmentDialog();
        addAssignmentDialog.setArguments(bundle);

        addAssignmentDialog.show(getFragmentManager(), "Add Course Dialog");
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
}
