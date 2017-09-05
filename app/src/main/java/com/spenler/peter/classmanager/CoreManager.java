package com.spenler.peter.classmanager;

import android.graphics.Color;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by peter on 5/30/17.
 */

public class CoreManager {
    static ArrayList<Course> courses = new ArrayList<>();
    static HashMap<String, Course> courseMap = new HashMap<>();
    static Course currentCourse;
    static Assignment currentAssignment;

    public static void addCourse(String name, double weight, int color){
        try {
            courses.add(new Course(name, weight, color, courses.size()));
            courseMap.put(name, courses.get(courses.size()));
        }
        catch(Exception e){
            Log.e("CoreManager/addCourse", "Course Could not be Added");
        }
    }

    public static ArrayList<Course> getCourses(){
        return courses;
    }

    public static int getCourseNum(){return courses.size();}

    public static int courseIndexByName(String name){
        for(int i = 0; i < courses.size(); i++){
            if(courses.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    public static Course getCourseByName(String name){return courseMap.get(name);}

    public static int darkenColor(int color, float amount){
        float[] hsv = new float[3];
        int darkColor;

        Color.colorToHSV(color, hsv);
        hsv[2] *= amount; // Format amount like "0.7f"
        darkColor = Color.HSVToColor(hsv);
        return darkColor;
    }

    public static int saveData(){
        FileOutputStream dfos, hfos; //Data and HashMap File Output Streams respectively
        try{
            dfos = MainActivity.context.openFileOutput("CourseData", MainActivity.context.MODE_PRIVATE);
            hfos = MainActivity.context.openFileOutput("HashData", MainActivity.context.MODE_PRIVATE);
            ObjectOutputStream doos = new ObjectOutputStream(dfos);
            ObjectOutputStream hoos = new ObjectOutputStream(hfos);
            doos.writeObject(courses);
            doos.close();
            hoos.writeObject(courseMap);
            hoos.close();
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
            dfis = MainActivity.context.openFileInput("CourseData");
            hfis = MainActivity.context.openFileInput("HashData");
            ObjectInputStream dois = new ObjectInputStream(dfis);
            courses = (ArrayList<Course>) dois.readObject();
            dois.close();
            ObjectInputStream hois = new ObjectInputStream(hfis);
            courseMap = (HashMap<String, Course>) hois.readObject();
            hois.close();
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
}
