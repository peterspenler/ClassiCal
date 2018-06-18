package com.spenler.peter.classmanager.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.spenler.peter.classmanager.activities.CourseViewActivity;
import com.spenler.peter.classmanager.activities.MainActivity;
import com.spenler.peter.classmanager.core.CoreManager;
import com.spenler.peter.classmanager.core.Course;
import com.spenler.peter.classmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by peter on 6/6/17.
 */

public class AddAssignmentDialog extends DialogFragment {

    private EditText nameEdit;
    private EditText worthEdit;
    private Spinner courseSpinner;
    private EditText dateEdit;
    private EditText timeEdit;
    private Calendar calendar;
    private Course currentCourse;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Context context = getActivity();
        final int MAIN_ACTIVITY = 0;
        final int COURSE_VIEW_ACTIVITY = 1;
        final int ASSIGNMENT_VIEW_DIALOG = 2;
        int type = -1;
        String title = "Add Assignment";

        if(context instanceof MainActivity){
            type = MAIN_ACTIVITY;
        }else if(context instanceof CourseViewActivity){
            type = COURSE_VIEW_ACTIVITY;
        }else if(context instanceof AssignmentViewDialog){
            type = ASSIGNMENT_VIEW_DIALOG;
            title = "Edit Assignment";
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_assignment, null);
        nameEdit = dialogView.findViewById(R.id.assignmentNameDialog);
        worthEdit = dialogView.findViewById(R.id.assignmentWorthDialog);
        courseSpinner = dialogView.findViewById(R.id.assignmentCourseSpinner);
        dateEdit = dialogView.findViewById(R.id.assignmentDueDateText);
        timeEdit = dialogView.findViewById(R.id.assignmentDueTimeText);
        calendar = Calendar.getInstance();
        currentCourse = CoreManager.getCurrentCourse();

        if(type == MAIN_ACTIVITY){
            List<String> courseNames = new ArrayList<>();
            final ArrayList<Course> courses = CoreManager.getCourses();

            //Initialize spinner
            for(int i = 0; i < courses.size(); i++){
                courseNames.add(courses.get(i).getName());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, courseNames);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courseSpinner.setAdapter(dataAdapter);
            Log.d("COURSE TITLE", CoreManager.getCurrentCourse().getName());
            courseSpinner.setSelection(CoreManager.courseIndexByName(CoreManager.getCurrentCourse().getName()));
            courseSpinner.setVisibility(View.VISIBLE);
        }else{
            courseSpinner.setVisibility(View.GONE);
        }

        //Initialize time and date to current time and date
        if(type != ASSIGNMENT_VIEW_DIALOG) {
            calendar.setTime(new Date());
        }else{
            calendar.setTime(CoreManager.getCurrentAssignment().getDueDate());
            nameEdit.setText(CoreManager.getCurrentAssignment().getName());
            worthEdit.setText(String.format(Locale.CANADA,"%f" ,CoreManager.getCurrentAssignment().getWeight()));
        }
        updateDate();
        updateTime();

        //initialize Date Picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };

        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Initialize time picker
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                updateTime();
            }

        };

        timeEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.Add_Dialog)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddAssignmentDialog.this.getDialog().dismiss();
                    }
                })
                .create();

        dialogView.setBackgroundColor(ContextCompat.getColor(context, R.color.md_blue_grey_700));

        final int innerType = type;
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if(innerType == MAIN_ACTIVITY) {
                                int courseIndex = CoreManager.courseIndexByName(courseSpinner.getSelectedItem().toString());
                                currentCourse = CoreManager.getCourses().get(courseIndex);
                            }
                            if(innerType != ASSIGNMENT_VIEW_DIALOG) {
                                currentCourse.addAssignment(nameEdit.getText().toString(), Float.parseFloat(worthEdit.getText().toString()), calendar.getTime(), currentCourse.getName(), currentCourse.getColor());
                            }else{
                                CoreManager.getCurrentAssignment().edit(nameEdit.getText().toString(), Float.parseFloat(worthEdit.getText().toString()), calendar.getTime());
                                ((AssignmentViewDialog) context).setValues();
                                ((AssignmentViewDialog) context).refreshBackground();
                            }
                            CoreManager.saveData();
                            alertDialog.dismiss();
                        }
                        catch(Exception e){
                            Toast.makeText(context.getApplicationContext() ,"All values must be filled!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    public AddAssignmentDialog newInstance(Bundle bundle){

        AddAssignmentDialog dialog = new AddAssignmentDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    private void updateDate() {
        String myFormat = "EEE MMM d, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEdit.setText(sdf.format(calendar.getTime()));
    }
    private void updateTime() {
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        timeEdit.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}