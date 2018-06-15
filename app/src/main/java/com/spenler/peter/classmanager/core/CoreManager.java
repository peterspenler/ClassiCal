package com.spenler.peter.classmanager.core;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.spenler.peter.classmanager.R;
import com.spenler.peter.classmanager.activities.MainActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by peter on 5/30/17.
 */

public class CoreManager {
    static private ArrayList<Course> courses = new ArrayList<>();
    //static private HashMap<String, Course> courseMap = new HashMap<>();
    static private Course currentCourse;
    static private Assignment currentAssignment;

    public static void addCourse(String name, double weight, int color){
        try {
            courses.add(new Course(name, weight, color, courses.size()));
            saveData();
            //courseMap.put(name, courses.get(courses.size()));
        }
        catch(Exception e){
            Log.e("CoreManager/addCourse", "Course Could not be Added");
        }
    }

    public static ArrayList<Course> getCourses(){
        return courses;
    }

    public static int getCourseNum(){return courses.size();}

    public static Assignment getCurrentAssignment() {
        return currentAssignment;
    }

    public static Course getCurrentCourse() {
        return currentCourse;
    }

    public static void setCurrentCourse(Course currentCourse) {
        CoreManager.currentCourse = currentCourse;
    }

    public static void setCurrentAssignment(Assignment currentAssignment) {
        CoreManager.currentAssignment = currentAssignment;
    }

    public static int courseIndexByName(String name){
        for(int i = 0; i < courses.size(); i++){
            if(courses.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    public static com.spenler.peter.classmanager.core.Course getCourseByName(String name){
        for(int i = 0; i < courses.size(); i++){
            if(courses.get(i).getName().equals(name)){
                return courses.get(i);
            }
        }
        return null;
        //return courseMap.get(name);
    }

    public static com.spenler.peter.classmanager.core.Course getCourseByIndex(int index){return courses.get(index);}

    public static int darkenColor(int color, float amount){
        float[] hsv = new float[3];
        int darkColor;

        Color.colorToHSV(color, hsv);
        hsv[2] *= amount; // Format amount like "0.7f"
        darkColor = Color.HSVToColor(hsv);
        return darkColor;
    }

    public static int desaturateColor(int color, float amount){
        float[] hsv = new float[3];
        int darkColor;
        Color.colorToHSV(color, hsv);

        if(hsv[1] <= 0.4){
            hsv[2] += 0.25;
        }

        hsv[1] *= amount; // Format amount like "0.7f"
        darkColor = Color.HSVToColor(hsv);
        return darkColor;
    }

    public static int saveData(){
        FileOutputStream dfos, hfos; //Data and HashMap File Output Streams respectively
        try{
            dfos = App.getContext().openFileOutput("CourseData.bin", Context.MODE_PRIVATE);
            //hfos = MainActivity.activity.openFileOutput("HashData", MainActivity.activity.MODE_PRIVATE);
            ObjectOutputStream doos = new ObjectOutputStream(dfos);
            //ObjectOutputStream hoos = new ObjectOutputStream(hfos);
            doos.writeObject(courses);
            doos.close();
            //hoos.writeObject(courseMap);
            //hoos.close();
            return 0;
        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
    public static int loadData(){
        FileInputStream dfis, hfis; //Data and HashMap File Input Streams respectively
        try {
            dfis = App.getContext().openFileInput("CourseData.bin");
            //hfis = MainActivity.activity.openFileInput("HashData");
            ObjectInputStream dois = new ObjectInputStream(dfis);
            courses = (ArrayList<Course>) dois.readObject();
            dois.close();
            //ObjectInputStream hois = new ObjectInputStream(hfis);
            //courseMap = (HashMap<String, Course>) hois.readObject();
           // hois.close();
            Log.d("Num loaded Courses", Integer.toString(courses.size()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    public static void sortCourses(){
        Collections.sort(courses);
    }

    public static double round(double number, int place){
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(place, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static float round(float number, int place){
        BigDecimal bd = new BigDecimal(Float.toString(number));
        bd = bd.setScale(place, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static boolean deleteAssignment(Assignment assignment, Course course) {
        int i = course.getAssignments().indexOf(assignment);
        if(i != -1){
            course.deleteAssignment(i);
            saveData();
            return true;
        }
        return false;
    }

    public static boolean removeCourse(Course course){
        int i = courses.indexOf(course);
        if(i != -1){
            for(int j = 0; j < course.numAssignments(); j++){
                course.deleteAssignment(j);
            }
            courses.remove(i);
            saveData();
            return true;
        }
        return false;
    }
/*
    public static int darkenColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }*/
}
