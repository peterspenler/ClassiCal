package com.spenler.peter.classmanager;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
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

    private LayoutInflater inflater;
    private View dialogView;
    private EditText nameEdit;
    private EditText worthEdit;
    private Spinner courseSpinner;
    private EditText dateEdit;
    private EditText timeEdit;
    private Calendar calendar;
    private Course currentCourse;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Add_Dialog);
        final Context context = getActivity();

        inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_add_assignment, null);
        nameEdit = (EditText) dialogView.findViewById(R.id.assignmentNameDialog);
        worthEdit = (EditText) dialogView.findViewById(R.id.assignmentWorthDialog);
        courseSpinner = (Spinner) dialogView.findViewById(R.id.assignmentCourseSpinner);
        dateEdit = (EditText) dialogView.findViewById(R.id.assignmentDueDateText);
        timeEdit = (EditText) dialogView.findViewById(R.id.assignmentDueTimeText);
        calendar = Calendar.getInstance();

        currentCourse = CoreManager.currentCourse;
        List<String> courseNames = new ArrayList<String>();
        final ArrayList<Course> courses = CoreManager.getCourses();

        //Initialize spinner
        for(int i = 0; i < courses.size(); i++){
            courseNames.add(courses.get(i).getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, courseNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(dataAdapter);
        Log.d("COURSE TITLE", CoreManager.currentCourse.getName());
        courseSpinner.setSelection(CoreManager.courseIndex(CoreManager.currentCourse.getName()));

        //Initialize time and date to current time and date
        calendar.setTime(new Date());
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

        //Initialize buttons
        builder.setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            int courseIndex = CoreManager.courseIndex(courseSpinner.getSelectedItem().toString());
                            CoreManager.getCourses().get(courseIndex).addAssignment(nameEdit.getText().toString(), Float.parseFloat(worthEdit.getText().toString()), calendar.getTime() ,currentCourse.getName(), currentCourse.getColor());
                        }
                        catch(Exception e){
                            Toast.makeText(dialogView.getContext() ,"All values must be filled!!!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddAssignmentDialog.this.getDialog().cancel();
                    }
                });

        dialogView.setBackgroundColor(ContextCompat.getColor(context, R.color.md_blue_grey_700));
        return builder.create();
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

}