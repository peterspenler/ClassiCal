package com.spenler.peter.classmanager;

import android.content.Intent;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by peter on 7/13/17.
 */

public class CourseAssignmentsFragment extends Fragment {
    private static final String TAG = "CourseAssignmentsFragment";
    private Course currentCourse;
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

        currentCourse = CoreManager.getCurrentCourse();

        CourseAssignmentsFragment.RVAdapter adapter = new CourseAssignmentsFragment.RVAdapter(currentCourse.getAssignments());
        rv.setAdapter(adapter);

        return view;
    }

    public static CourseAssignmentsFragment newInstance (Course course){
        CourseAssignmentsFragment caf = new CourseAssignmentsFragment();
        Bundle bundle = new Bundle();
        CoreManager.setCurrentCourse(course);
        //bundle.putParcelable("course",course);
        caf.setArguments(bundle);
        return caf;
    }

    public class RVAdapter extends RecyclerView.Adapter<CourseAssignmentsFragment.RVAdapter.AssignmentViewHolder>{

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

            AssignmentViewHolder(final View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.card_view);
                assignmentName = itemView.findViewById(R.id.assignmentTitleText);
                assignmentValue = itemView.findViewById(R.id.assignmentValueText);
                assignmentMark = itemView.findViewById(R.id.assignmentMarkText);
                assignmentDue = itemView.findViewById(R.id.assignmentDueDateText);
                assignmentCompleteMark = itemView.findViewById(R.id.assignmentCompleteMark);

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        CoreManager.setCurrentAssignment(assignments.get(getAdapterPosition()));
                        startActivity(new Intent(itemView.getContext(), AssignmentViewDialog.class));
                    }
                });
            }
        }
        @NonNull
        public CourseAssignmentsFragment.RVAdapter.AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_layout, parent, false);
            return new CourseAssignmentsFragment.RVAdapter.AssignmentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseAssignmentsFragment.RVAdapter.AssignmentViewHolder holder, int position) {
            holder.currentAssignment = assignments.get(position);
            holder.assignmentName.setText(assignments.get(position).getName());
            holder.assignmentValue.setText(String.valueOf(CoreManager.round(assignments.get(position).getWeight(),2)) + "%");
            holder.assignmentMark.setText(assignments.get(position).getMarkString());
            holder.assignmentDue.setText("Due: " + sdf.format(assignments.get(position).getDueDate()) + " at " + stf.format(assignments.get(position).getDueDate()));
            if(assignments.get(position).isFinished()){
                holder.assignmentCompleteMark.setVisibility(View.VISIBLE);
            }else{
                holder.assignmentCompleteMark.setVisibility(View.INVISIBLE);
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

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
