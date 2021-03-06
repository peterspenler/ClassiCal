package com.spenler.peter.classmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.spenler.peter.classmanager.R;
import com.spenler.peter.classmanager.core.Assignment;
import com.spenler.peter.classmanager.core.CoreManager;
import com.spenler.peter.classmanager.core.Course;
import com.spenler.peter.classmanager.dialogs.AssignmentViewDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by peter on 4/29/17.
 */

public class AssignmentFragment extends Fragment {
    private List<Assignment> assignments;
    private AssignmentFragment.RVAdapter adapter;
    SimpleDateFormat sdf;
    SimpleDateFormat stf;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        assert container != null;
        LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
        rv.setLayoutManager(llm);
        sdf = new SimpleDateFormat("EEE MMM d", Locale.US);
        stf = new SimpleDateFormat("h:mm a", Locale.US);
        assignments = new ArrayList<>();

        for(int i = 0; i < CoreManager.getCourseNum(); i++){
            Course course = CoreManager.getCourseByIndex(i);
            if(course.getAssignments() != null) {
                assignments.addAll(course.getAssignments());
            }
        }
        Collections.sort(assignments);

        adapter = new AssignmentFragment.RVAdapter(assignments);
        rv.setAdapter(adapter);

        return view;
    }

    public void refreshFragment(){
        Collections.sort(assignments);
        adapter.notifyDataSetChanged();
    }

    public static CourseAssignmentsFragment newInstance (Course course){
        CourseAssignmentsFragment caf = new CourseAssignmentsFragment();
        Bundle bundle = new Bundle();
        CoreManager.setCurrentCourse(course);
        //bundle.putParcelable("course",course);
        caf.setArguments(bundle);
        return caf;
    }

    public class RVAdapter extends RecyclerView.Adapter<AssignmentFragment.RVAdapter.AssignmentViewHolder>{

        List<Assignment> assignments;

        RVAdapter(List<Assignment> assignments){
            this.assignments = assignments;
        }

        class AssignmentViewHolder extends RecyclerView.ViewHolder{
            CardView cv;
            TextView assignmentName;
            TextView assignmentValue;
            TextView assignmentMark;
            TextView assignmentDue;
            TextView assignmentCompleteMark;
            Assignment currentAssignment;
            View iv;

            AssignmentViewHolder(final View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.card_view);
                assignmentName = itemView.findViewById(R.id.assignmentTitleText);
                assignmentValue = itemView.findViewById(R.id.assignmentValueText);
                assignmentMark = itemView.findViewById(R.id.assignmentMarkText);
                assignmentDue = itemView.findViewById(R.id.assignmentDueDateText);
                assignmentCompleteMark = itemView.findViewById(R.id.assignmentCompleteMark);
                iv = itemView;

               itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        CoreManager.setCurrentAssignment(assignments.get(getAdapterPosition()));
                        startActivityForResult(new Intent(itemView.getContext(), AssignmentViewDialog.class), 1);
                    }
                });
            }
        }
        @NonNull
        public AssignmentFragment.RVAdapter.AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_layout, parent, false);
            return new AssignmentFragment.RVAdapter.AssignmentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AssignmentFragment.RVAdapter.AssignmentViewHolder holder, int position) {
            holder.currentAssignment = assignments.get(position);
            holder.assignmentName.setText(assignments.get(position).getName());
            holder.assignmentValue.setText(String.valueOf(CoreManager.round(assignments.get(position).getWeight(),2)) + "%");
            holder.assignmentMark.setText(assignments.get(position).getMarkString());
            holder.assignmentDue.setText(CoreManager.timeUntilDueString(assignments.get(position).getDueDate()));
            if(assignments.get(position).isFinished()){
                holder.iv.setBackgroundColor(CoreManager.desaturateColor(assignments.get(position).getColor(), 0.4f));
                holder.assignmentCompleteMark.setVisibility(View.VISIBLE);
                holder.assignmentDue.setVisibility(View.GONE);
            }else {
                holder.iv.setBackgroundColor(assignments.get(position).getColor());
                holder.assignmentCompleteMark.setVisibility(View.GONE);
                holder.assignmentDue.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return assignments.size();
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }
}
