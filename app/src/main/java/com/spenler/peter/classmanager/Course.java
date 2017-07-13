package com.spenler.peter.classmanager;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peter on 5/10/17.
 */

public class Course implements Parcelable {

    private String name;
    private int id, color;
    double weight, grade;
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private ArrayList<Lecture> lectures = new ArrayList<>();

    public Course (String name, double weight, int color, int id) throws Exception{
        if(name.equals(""))
            throw new Exception("empty name");
        this.name = name;
        this.weight = weight;
        this.id = id;
        this.color = color;
        grade = 100;
    }

    public int getID(){return id;}

    public int getColor(){return color;}

    public String getName(){return name;}

    public double getGrade(){return grade;}

    public char getChar(){return Character.toUpperCase(name.charAt(0));}

    public ArrayList<Assignment> getAssignments(){return (ArrayList<Assignment>) assignments.clone();}

    public String assignmentsLeft(){
        int size = assignments.size();
        if(size == 1)
            return "1 Assignment Left";
        else
            return String.valueOf(size) + " Assignments Left";
    }

    public ArrayList<Lecture> getLectures(){return (ArrayList<Lecture>) lectures.clone();}

    public void addAssignment (String name, int weight, Date dueDate, String course, int color){
        assignments.add(new Assignment(name, weight, dueDate, course, color));
        Log.d("Adding Assignment", assignments.get(assignments.size()).getName());
    }
    public void addLecture(Date start, Date end, Color color){
        lectures.add(new Lecture(start, end, color));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeInt(this.color);
        dest.writeDouble(this.weight);
        dest.writeDouble(this.grade);
        dest.writeList(this.assignments);
        dest.writeList(this.lectures);
    }

    protected Course(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.color = in.readInt();
        this.weight = in.readDouble();
        this.grade = in.readDouble();
        this.assignments = new ArrayList<Assignment>();
        in.readList(this.assignments, Assignment.class.getClassLoader());
        this.lectures = new ArrayList<Lecture>();
        in.readList(this.lectures, Lecture.class.getClassLoader());
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
