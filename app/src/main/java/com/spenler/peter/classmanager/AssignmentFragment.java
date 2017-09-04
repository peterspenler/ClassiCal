package com.spenler.peter.classmanager;

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

        for(int i=0; i < (CoreManager.courses.size() - 1);i++){
            Course course = CoreManager.courses.get(i);
            if(course.getAssignments() != null) {
                assignments.addAll(course.getAssignments());
            }
        }

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
            TextView assignmentDue;
            Assignment currentAssignment;

            public AssignmentViewHolder(final View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.card_view);
                assignmentName = (TextView) itemView.findViewById(R.id.assignmentTitleText);
                assignmentValue = (TextView) itemView.findViewById(R.id.assignmentValueText);
                assignmentDue = (TextView) itemView.findViewById(R.id.assignmentDueDateText);

               /* itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("assignment",assignments.get(getAdapterPosition()));

                        Intent intent = new Intent(itemView.getContext(), CourseViewActivity.class);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                });*/
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
            holder.assignmentValue.setText(String.valueOf(assignments.get(position).getWeight()) + "%");
            holder.assignmentDue.setText("Due: " + sdf.format(assignments.get(position).getDueDate()) + " at " + stf.format(assignments.get(position).getDueDate()));
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
