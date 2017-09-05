package com.spenler.peter.classmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.util.Random;

/**
 * Created by peter on 5/28/17.
 */

public class AddCourseDialog extends DialogFragment{

    int mSelectedColor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Add_Dialog);
        final Context context = getActivity();

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_course, null);
        final EditText nameEdit = (EditText) dialogView.findViewById(R.id.nameDialog);
        final EditText weightEdit = (EditText) dialogView.findViewById(R.id.WeightDialog);
        final ImageButton colorButton = (ImageButton) dialogView.findViewById(R.id.colorButton);
        final GradientDrawable colorCircle = (GradientDrawable) colorButton.getDrawable();
        final Random rand = new Random();

        int[] mColors = getResources().getIntArray(R.array.demo_colors);
        mSelectedColor = mColors[rand.nextInt(mColors.length)];
        colorCircle.setColor(mSelectedColor);

        final ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_title,
                mColors, mSelectedColor, 5, ColorPickerDialog.SIZE_SMALL, true);

        colorButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        mSelectedColor = color;
                        colorCircle.setColor(color);
                    }
                });
                dialog.show(getFragmentManager(), "color_dialog_test");
                Log.d("Color", String.valueOf(mSelectedColor));
            }

        });

        builder.setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if(CoreManager.getCourseByName(nameEdit.getText().toString()) == null){
                                if(weightEdit.getText().toString().equals(""))
                                    CoreManager.addCourse(nameEdit.getText().toString(), 0.5, mSelectedColor);
                                else
                                    CoreManager.addCourse(nameEdit.getText().toString(), Float.parseFloat(weightEdit.getText().toString()), mSelectedColor);
                            }
                            else
                                Toast.makeText(dialogView.getContext() ,"Course already exists", Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(dialogView.getContext() ,"All values must be filled", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddCourseDialog.this.getDialog().cancel();
                    }
                });

        dialogView.setBackgroundColor(ContextCompat.getColor(context, R.color.md_blue_grey_700));
        return builder.create();
    }

    public AddCourseDialog newInstance(Bundle bundle){
        AddCourseDialog dialog = new AddCourseDialog();
        dialog.setArguments(bundle);

        return dialog;
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
