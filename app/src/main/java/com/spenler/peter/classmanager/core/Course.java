package com.spenler.peter.classmanager.core;

import android.graphics.Color;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by peter on 5/10/17.
 */

public class Course implements Serializable, Comparable<Course>{

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

    public double getGrade(){
        double totalPercent = 0;
        double totalMarks = 0;
        for(int i = 0; i < assignments.size(); i++){
            if(assignments.get(i).isFinished() && assignments.get(i).isMarked()){
                totalPercent += assignments.get(i).getWeight();
                totalMarks += assignments.get(i).getMark() * (assignments.get(i).getWeight() / 100);
            }
        }
        if((totalMarks == 0) && (totalPercent == 0))
            grade = 100;
        else
            grade = 100 * (totalMarks / totalPercent);
        return grade;
    }

    public double getPredictedGrade(){
        double totalPercent = 0;
        double totalMarks = 0;
        double predictedGrade = 0;
        for(int i = 0; i < assignments.size(); i++){
            if(assignments.get(i).isMarked()){
                totalPercent += assignments.get(i).getWeight();
                totalMarks += assignments.get(i).getMark() * (assignments.get(i).getWeight() / 100);
            }
        }
        if((totalMarks == 0) && (totalPercent == 0))
            predictedGrade = 100;
        else
            predictedGrade = 100 * (totalMarks / totalPercent);
        return predictedGrade;
    }

    public char getChar(){return Character.toUpperCase(name.charAt(0));}

    public ArrayList<Assignment> getAssignments(){return (ArrayList<Assignment>) assignments.clone();}

    public int numAssignments(){return assignments.size();}

    public Assignment getAssignment(int i){return assignments.get(i);}

    public void deleteAssignment(int i){
        assignments.remove(i);
    }

    public String assignmentsLeft(){
        int size = 0;
        for(int i = 0; i < assignments.size(); i++){
            if(!assignments.get(i).isFinished()){
                size++;
            }
        }
        if(size == 1)
            return "1 Assignment Left";
        else if(size == 0)
            return "No Assignments Left";
        else
            return String.valueOf(size) + " Assignments Left";
    }

    public ArrayList<Lecture> getLectures(){return (ArrayList<Lecture>) lectures.clone();}

    public void addAssignment (String name, float weight, Date dueDate, String course, int color){
        assignments.add(new Assignment(name, weight, dueDate, course, color));
        Collections.sort(assignments);
        Log.d("Adding Assignment", assignments.get(assignments.size() - 1).getName());
    }
    public void addLecture(Date start, Date end, Color color){
        lectures.add(new Lecture(start, end, color));
    }

    @Override
    public int compareTo(Course other){
        if(getName().compareTo(other.getName()) > 0)
            return 1;
        else if(getName().compareTo(other.getName()) < 0)
            return -1;
        else
            return 0;
    }

/*

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
        this.assignments = new ArrayList<>();
        in.readList(this.assignments, Assignment.class.getClassLoader());
        this.lectures = new ArrayList<>();
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
*/
}
