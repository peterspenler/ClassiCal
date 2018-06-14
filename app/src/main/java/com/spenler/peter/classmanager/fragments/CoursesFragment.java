package com.spenler.peter.classmanager.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spenler.peter.classmanager.core.CoreManager;
import com.spenler.peter.classmanager.core.Course;
import com.spenler.peter.classmanager.R;
import com.spenler.peter.classmanager.activities.CourseViewActivity;

import java.util.List;

/**
 * Created by peter on 4/29/17.
 */

public class CoursesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        assert container != null;
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

        class CourseViewHolder extends RecyclerView.ViewHolder{
            CardView cv;
            TextView courseName;
            TextView courseGrade;
            TextView courseLatestDate;
            TextView courseAssignments;
            TextView courseChar;
            ImageView courseImage;
            GradientDrawable courseSquare;
            Course currentCourse;

            CourseViewHolder(final View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.card_view);
                courseName = itemView.findViewById(R.id.courseTitleText);
                courseLatestDate = itemView.findViewById(R.id.courseDueDateText);
                courseGrade = itemView.findViewById(R.id.courseGradeText);
                courseAssignments = itemView.findViewById(R.id.courseAssignmentsText);
                courseChar = itemView.findViewById(R.id.courseChar);
                courseImage = itemView.findViewById(R.id.courseSquare);
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
        @NonNull
        public RVAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_layout, parent, false);
            return new CourseViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
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
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }
}
