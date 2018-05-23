package com.spenler.peter.classmanager;

import android.content.Intent;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by peter on 4/29/17.
 */

public class AssignmentFragment extends Fragment {
    private static final String TAG = "AssignmentFragment";
    private List<Assignment> assignments;
    SimpleDateFormat sdf;
    SimpleDateFormat stf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
        rv.setLayoutManager(llm);
        sdf = new SimpleDateFormat("EEE MMM d", Locale.US);
        stf = new SimpleDateFormat("h:mm a", Locale.US);
        assignments = new ArrayList<>();

        for(int i=0; i < CoreManager.courses.size();i++){
            Course course = CoreManager.courses.get(i);
            if(course.getAssignments() != null) {
                assignments.addAll(course.getAssignments());
            }
        }
        Collections.sort(assignments);

        AssignmentFragment.RVAdapter adapter = new AssignmentFragment.RVAdapter(assignments);
        rv.setAdapter(adapter);

        return view;
    }

    public static CourseAssignmentsFragment newInstance (Course course){
        CourseAssignmentsFragment caf = new CourseAssignmentsFragment();
        Bundle bundle = new Bundle();
        CoreManager.currentCourse = course;
        //bundle.putParcelable("course",course);
        caf.setArguments(bundle);
        return caf;
    }

    public class RVAdapter extends RecyclerView.Adapter<AssignmentFragment.RVAdapter.AssignmentViewHolder>{

        List<Assignment> assignments;

        RVAdapter(List<Assignment> assignments){
            this.assignments = assignments;
        }

        public class AssignmentViewHolder extends RecyclerView.ViewHolder{
            CardView cv;
            TextView assignmentName;
            TextView assignmentValue;
            TextView assignmentMark;
            TextView assignmentDue;
            TextView assignmentCompleteMark;
            Assignment currentAssignment;
            View iv;

            public AssignmentViewHolder(final View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.card_view);
                assignmentName = (TextView) itemView.findViewById(R.id.assignmentTitleText);
                assignmentValue = (TextView) itemView.findViewById(R.id.assignmentValueText);
                assignmentMark = (TextView) itemView.findViewById(R.id.assignmentMarkText);
                assignmentDue = (TextView) itemView.findViewById(R.id.assignmentDueDateText);
                assignmentCompleteMark = (TextView) itemView.findViewById(R.id.assignmentCompleteMark);
                iv = itemView;

               itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        CoreManager.currentAssignment = assignments.get(getAdapterPosition());
                        startActivity(new Intent(itemView.getContext(), AssignmentViewActivity.class));
                    }
                });
            }
        }
        public AssignmentFragment.RVAdapter.AssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_layout, parent, false);
            AssignmentFragment.RVAdapter.AssignmentViewHolder cvh = new AssignmentFragment.RVAdapter.AssignmentViewHolder(v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(AssignmentFragment.RVAdapter.AssignmentViewHolder holder, int position) {
            holder.currentAssignment = assignments.get(position);
            holder.assignmentName.setText(assignments.get(position).getName());
            holder.assignmentValue.setText(String.valueOf(CoreManager.round(assignments.get(position).getWeight(),2)) + "%");
            holder.assignmentMark.setText(assignments.get(position).getMarkString());
            holder.assignmentDue.setText("Due: " + sdf.format(assignments.get(position).getDueDate()) + " at " + stf.format(assignments.get(position).getDueDate()));
           // ArrayList<Course> DEBUG = CoreManager.courses;
            //int color = CoreManager.courses.get(CoreManager.courseIndex(assignments.get(position).getCourseName())).getColor();
            holder.iv.setBackgroundColor(assignments.get(position).getColor());
            if(assignments.get(position).isFinished()){
                holder.assignmentCompleteMark.setVisibility(View.VISIBLE);
            }else {
                holder.assignmentCompleteMark.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
         //   try {
            Log.d("DEBUG", Integer.toString(assignments.size()));
                return assignments.size();

         /*   }
            catch (Exception e){
                return 0;
            }*/
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }
}
