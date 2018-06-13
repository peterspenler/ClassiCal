package com.spenler.peter.classmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Gaming PC on 2017-09-05.
 */

public class AssignmentViewActivity extends AppCompatActivity{
    public Activity activity;
    public View view;
    private Assignment assignment;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(assignment.getName());
        actionBar.setBackgroundDrawable(new ColorDrawable(assignment.getColor()));
        int darkColor = CoreManager.darkenColor(assignment.getColor(), 0.7f);
        activity.getWindow().setStatusBarColor(darkColor);

        assignmentViewName = (TextView)findViewById(R.id.assignmentViewName);
        assignmentViewDueDate = (TextView)findViewById(R.id.assignmentViewDueDate);
        assignmentViewDueIn = (TextView)findViewById(R.id.assignmentViewDueIn);
        assignmentViewWorth = (TextView)findViewById(R.id.assignmentViewWorth);
        assignmentViewMarkTitle = (TextView)findViewById(R.id.assignmentViewMarkTitle);
        assignmentViewMark = (TextView)findViewById(R.id.assignmentViewMark);
        assignmentViewCheckImage = (ImageView)findViewById(R.id.assignmentViewCheckImage);
        completeAssignmentButton = (Button)findViewById(R.id.assignmentCompletedButton);

        sdf = new SimpleDateFormat("EEE MMM d", Locale.US);
        stf = new SimpleDateFormat("h:mm a", Locale.US);

        setValues();
    }

    private void setValues(){
        assignmentViewName.setText("For " + assignment.getCourseName());
        assignmentViewDueDate.setText("Due: " + sdf.format(assignment.getDueDate()) + " at " + stf.format(assignment.getDueDate()));
        assignmentViewDueIn.setText("Due in 4 days");
        assignmentViewWorth.setText(String.valueOf(CoreManager.round(assignment.getWeight(),2)) + "%");
        if(assignment.isFinished())
            assignmentViewMarkTitle.setText("Mark");
        else
            assignmentViewMarkTitle.setText("Predicted Mark");
        assignmentViewMark.setText(assignment.getMarkString());
        if(assignment.isFinished()) {
            assignmentViewCheckImage.setVisibility(View.VISIBLE);
            completeAssignmentButton.setText("Un-complete Assignment");
        }else {
            assignmentViewCheckImage.setVisibility(View.INVISIBLE);
            completeAssignmentButton.setText("Complete Assignment");
        }
    }

    public void completeAssignment(View view) {
        CoreManager.getCurrentAssignment().toggleFinished();
        setValues();
    }

    public void editAssignment(View view) {
    }

    public void deleteAssignment(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Assignment")
                .setMessage("Are you sure you want to delete " + assignment.getName() + "?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //try {
                            CoreManager.deleteAssignment(assignment, CoreManager.getCourseByName(assignment.getCourseName()));
                            finish();
                        //}catch (Exception e){
                        //    Toast.makeText(MainActivity.activity, "Unable to delete assignment", Toast.LENGTH_SHORT).show();
                        //}
                    }
                })
                .create();
        dialog.show();
    }

    public void markAssignment(View view) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_change_mark, null);
        builder1.setView(layout);
        final EditText input1 = (EditText) layout.findViewById(R.id.changeMarkDialogEdit);
        Float currentMark = CoreManager.getCurrentAssignment().getMark();
        if(currentMark >= 0) {
            input1.setText(Float.toString(currentMark));
        }
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.activity, "Updated mark", Toast.LENGTH_SHORT).show();
                CoreManager.getCurrentAssignment().setMark(Float.parseFloat(input1.getText().toString()));
                setValues();
            }
        });
        Dialog dialog = builder1.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_blue_grey_700);
        dialog.show();
    }
}
