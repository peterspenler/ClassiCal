package com.spenler.peter.classmanager;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by peter on 4/29/17.
 */

public class CoursesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
        rv.setLayoutManager(llm);

        CoreManager.sortCourses();
        RVAdapter adapter = new RVAdapter(CoreManager.getCourses());
        rv.setAdapter(adapter);

        return view;
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CourseViewHolder>{

        List<Course> courses;

        RVAdapter(List<Course> courses){
            this.courses = courses;
        }

        public class CourseViewHolder extends RecyclerView.ViewHolder{
            CardView cv;
            TextView courseName;
            TextView courseGrade;
            TextView courseLatestDate;
            TextView courseAssignments;
            TextView courseChar;
            ImageView courseImage;
            GradientDrawable courseSquare;
            Course currentCourse;

            public CourseViewHolder(final View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.card_view);
                courseName = (TextView) itemView.findViewById(R.id.courseTitleText);
                courseLatestDate = (TextView) itemView.findViewById(R.id.courseDueDateText);
                courseGrade = (TextView) itemView.findViewById(R.id.courseGradeText);
                courseAssignments = (TextView) itemView.findViewById(R.id.courseAssignmentsText);
                courseChar = (TextView) itemView.findViewById(R.id.courseChar);
                courseImage = (ImageView) itemView.findViewById(R.id.courseSquare);
                courseSquare = (GradientDrawable) courseImage.getDrawable();

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Bundle bundle = new Bundle();
                        //bundle.putParcelable("course",courses.get(getAdapterPosition()));
                        CoreManager.setCurrentCourse(courses.get(getAdapterPosition()));
                        Intent intent = new Intent(itemView.getContext(), CourseViewActivity.class);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                });
            }
        }
        public RVAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_layout, parent, false);
            CourseViewHolder cvh = new CourseViewHolder(v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(CourseViewHolder holder, int position) {
            holder.currentCourse = courses.get(position);
            holder.courseName.setText(courses.get(position).getName());
            holder.courseGrade.setText(String.valueOf(CoreManager.round(courses.get(position).getGrade(), 2)) + "%");
            holder.courseChar.setText(""+courses.get(position).getChar());
            holder.courseAssignments.setText(courses.get(position).assignmentsLeft());
            holder.courseSquare.setColor(courses.get(position).getColor());
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }
}
