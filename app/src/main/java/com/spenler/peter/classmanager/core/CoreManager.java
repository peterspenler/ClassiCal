package com.spenler.peter.classmanager.core;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by peter on 5/30/17.
 */

public class CoreManager {
    static private ArrayList<Course> courses = new ArrayList<>();
    static private Course currentCourse;
    static private Assignment currentAssignment;

    public static void addCourse(String name, double weight, int color){
        try {
            courses.add(new Course(name, weight, color, courses.size()));
            saveData();
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

    public static void saveData(){
        FileOutputStream dfos;
        try{
            dfos = App.getContext().openFileOutput("CourseData.bin", Context.MODE_PRIVATE);
            ObjectOutputStream doos = new ObjectOutputStream(dfos);
            doos.writeObject(courses);
            doos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void loadData(){
        FileInputStream dfis;
        try {
            dfis = App.getContext().openFileInput("CourseData.bin");
            ObjectInputStream dois = new ObjectInputStream(dfis);
            courses = (ArrayList<Course>) dois.readObject();
            dois.close();
            Log.d("Num loaded Courses", Integer.toString(courses.size()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

    public static String timeUntilDueString(Date duedate){
        Date time = Calendar.getInstance().getTime();
        long compare = stripSeconds(duedate).getTime() - stripSeconds(time).getTime();

        if(compare > 0){
            if(compare < 60000){
                return "Due in < 1 minute";
            }if(compare < 120000){
                return "Due in 1 minute";
            }else if(compare < 3600000){
                return "Due in " + (int)Math.floor(((double)compare)/60000.0f) + " minutes";
            }else if(compare < 7200000){
                return "Due in 1 hour";
            }else if(compare < 86400000){
                return "Due in " + (int)Math.floor(((double)compare)/3600000.0f) + " hours";
            }else if(compare < 172800000){
                return "Due in 1 day";
            }else if(compare < 2592000000L){
                return "Due in " + (int)Math.floor(((double)compare)/86400000.0f) + " days";
            }else{
                return "Due in over a month";
            }
        }else{
            if(compare > -60000){
                return "< 1 minute overdue";
            }if(compare > -120000){
                return "1 minute overdue";
            }else if(compare > -3600000){
                return (int)Math.floor(((double)compare * -1)/60000.0f) + " minutes overdue";
            }else if(compare > -7200000){
                return "1 hour overdue";
            }else if(compare > -86400000){
                return (int)Math.floor(((double)compare * -1)/3600000.0f) + " hours overdue";
            }else if(compare > -172800000){
                return "1 day overdue";
            }else if(compare > -2592000000L){
                return (int)Math.floor(((double)compare * -1)/86400000.0f) + " days overdue";
            }else{
                return "Over a month overdue";
            }
        }
    }

    public static Date stripSeconds(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
