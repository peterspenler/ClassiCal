package com.spenler.peter.classmanager;

import android.graphics.Color;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by peter on 5/30/17.
 */

public class CoreManager {
    static ArrayList<Course> courses = new ArrayList<>();
    static HashMap<String, Integer> courseMap = new HashMap<>();

    public static void addCourse(String name, double weight, int color){
        try {
            courseMap.put(name, courses.size());
            courses.add(new Course(name, weight, color, courses.size()));
        }
        catch(Exception e){
            Log.e("CoreManager/addCourse", "Course Could not be Added");
        }
    }

    public static ArrayList<Course> getCourses(){
        return courses;
    }

    public static int getCourseNum(){return courses.size();}

    public static Integer courseIndex(String name){return courseMap.get(name);}

    public static int darkenColor(int color, float amount){
        float[] hsv = new float[3];
        int darkColor;

        Color.colorToHSV(color, hsv);
        hsv[2] *= amount; // Format amount like "0.7f"
        darkColor = Color.HSVToColor(hsv);
        return darkColor;
    }

    public static int saveData(){
        FileOutputStream fos;
        try{
            fos = MainActivity.context.openFileOutput("CourseData", MainActivity.context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(courses);
            oos.close();
            return 0;
        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }
    public static int loadData(){
        FileInputStream fis;
        try {
            fis = MainActivity.context.openFileInput("CourseData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Course> loadCourses = (ArrayList<Course>) ois.readObject();
            ois.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return 1;
    }
}
