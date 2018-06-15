package com.spenler.peter.classmanager.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spenler.peter.classmanager.R;
import com.spenler.peter.classmanager.activities.MainActivity;
import com.spenler.peter.classmanager.core.App;
import com.spenler.peter.classmanager.core.Assignment;
import com.spenler.peter.classmanager.core.CoreManager;
import com.spenler.peter.classmanager.fragments.AssignmentFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Gaming PC on 2017-09-05.
 */

public class AssignmentViewDialog extends /*AppCompat*/Activity{
    public Activity activity;
    public View view;
    private Assignment assignment;
    private AssignmentFragment parent;

    SimpleDateFormat sdf;
    SimpleDateFormat stf;

    private TextView assignmentViewName;
    private TextView assignmentViewDueDate;
    private TextView assignmentViewDueIn;
    private TextView assignmentViewWorth;
    private TextView assignmentViewMarkTitle;
    private TextView assignmentViewMark;
    private ImageView assignmentViewCheckImage;

    private Button completeAssignmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);
        activity = this;
        assignment = CoreManager.getCurrentAssignment();
        parent = MainActivity.getAssignmentFragment();
        //android.app.ActionBar actionBar = getActionBar();
        //actionBar.setTitle(assignment.getName());
        //actionBar.setBackgroundDrawable(new ColorDrawable(assignment.getColor()));
        //int darkColor = CoreManager.darkenColor(assignment.getColor(), 0.7f);
        //activity.getWindow().setStatusBarColor(darkColor);

        getWindow().setBackgroundDrawableResource(R.color.md_blue_grey_700);

        TextView title = findViewById(R.id.dialogActionBarTitle);
        ImageView actionBar = findViewById(R.id.dialogActionBar);
        title.setText(assignment.getName());
        ((GradientDrawable) actionBar.getDrawable()).setColor(assignment.getColor());

        assignmentViewName = findViewById(R.id.assignmentViewName);
        assignmentViewDueDate = findViewById(R.id.assignmentViewDueDate);
        assignmentViewDueIn = findViewById(R.id.assignmentViewDueIn);
        assignmentViewWorth = findViewById(R.id.assignmentViewWorth);
        assignmentViewMarkTitle = findViewById(R.id.assignmentViewMarkTitle);
        assignmentViewMark = findViewById(R.id.assignmentViewMark);
        assignmentViewCheckImage = findViewById(R.id.assignmentViewCheckImage);
        completeAssignmentButton = findViewById(R.id.assignmentCompletedButton);

        sdf = new SimpleDateFormat("EEE MMM d", Locale.US);
        stf = new SimpleDateFormat("h:mm a", Locale.US);

        setValues();
    }

    private void setValues(){
        assignmentViewName.setText("For: " + assignment.getCourseName());
        assignmentViewDueDate.setText("Due: " + sdf.format(assignment.getDueDate()) + " at " + stf.format(assignment.getDueDate()));
        assignmentViewDueIn.setText(CoreManager.timeUntilDueString(assignment.getDueDate()));
        assignmentViewWorth.setText(String.valueOf(CoreManager.round(assignment.getWeight(),2)) + "%");
        if(assignment.isFinished())
            assignmentViewMarkTitle.setText("Mark");
        else
            assignmentViewMarkTitle.setText("Predicted Mark");
        assignmentViewMark.setText(assignment.getMarkString());
        if(assignment.isFinished()) {
            assignmentViewDueIn.setVisibility(View.GONE);
            assignmentViewCheckImage.setVisibility(View.VISIBLE);
            completeAssignmentButton.setText("Un-complete Assignment");
        }else {
            assignmentViewDueIn.setVisibility(View.VISIBLE);
            assignmentViewCheckImage.setVisibility(View.GONE);
            completeAssignmentButton.setText("Complete Assignment");
        }
    }

    public void completeAssignment(View view) {
        CoreManager.getCurrentAssignment().toggleFinished();
        CoreManager.saveData();
        setValues();
        //TODO Also refresh CourseViewActivity
        parent.refreshFragment();
    }

    public void editAssignment(View view) {
    }

    public void deleteAssignment(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Add_Dialog)
                .setTitle("Delete Assignment")
                .setMessage("Are you sure you want to delete " + assignment.getName() + "?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            if(CoreManager.deleteAssignment(assignment, CoreManager.getCourseByName(assignment.getCourseName()))) {
                                finish();
                            }
                        }catch (NullPointerException e){
                            Toast.makeText(App.getContext(), "Unable to delete assignment", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void markAssignment(View view) {

        final AssignmentViewDialog thisActivity = this;

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lllp.setMargins(50, 0, 50, 0);
        final EditText input = new EditText(this);
        if(assignment.getMark() >= 0)
            input.setText(Float.toString(assignment.getMark()));
        input.setLayoutParams(lllp);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setId(View.generateViewId());
        final int id = input.getId();
        container.addView(input);

        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.Add_Dialog)
                .setTitle("Enter Mark")
                .setPositiveButton("Ok", null)
                .setView(container)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                final EditText edit = alertDialog.findViewById(id);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            assignment.setMark(Float.parseFloat(edit.getText().toString()));
                            alertDialog.dismiss();
                            thisActivity.setValues();
                            CoreManager.saveData();
                        }catch(NumberFormatException e){
                            Toast.makeText(App.getContext(), "Mark must be decimal number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
}
