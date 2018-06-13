package com.spenler.peter.classmanager;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peter on 5/10/17.
 */

 class Assignment implements Serializable, Comparable<Assignment>{

    private String name, course;
    private int color;
    private float weight, mark;
    private Date dueDate;
    private boolean finished;

    Assignment(String name, float weight, Date dueDate, String course, int color){
        this.name = name;
        this.weight = weight;
        this.dueDate = dueDate;
        this.course = course;
        this.color = color;
        this.mark = -1;
        this.finished = false;
    }

    public float setMark(float mark){
        if((mark >= 0) && (mark <= 100)) {
            this.mark = mark;
            return mark;
        }else{
            return -1;
        }
    }

    public String getName(){
        return name;
    }

    float getWeight(){
        return weight;
    }

    Date getDueDate(){
        return dueDate;
    }

    public String getCourseName() {return course;}

    public int getColor() {return color;}

    public boolean isFinished() {return finished;}

    public boolean toggleFinished(){
        finished = !finished;
        return finished;
    }

    public float getMark(){return mark;}

    public String getMarkString(){
        if(mark < 0)
            return "None";
        else
            return String.valueOf(CoreManager.round(mark,2)) + "%";
    }

    public boolean isMarked(){
        if(mark < 0)
            return false;
        return true;
    }

    /*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.course);
        dest.writeFloat(this.weight);
        dest.writeFloat(this.mark);
        dest.writeInt(this.color);
        dest.writeLong(this.dueDate != null ? this.dueDate.getTime() : -1);
    }

    protected Assignment(Parcel in) {
        this.name = in.readString();
        this.course = in.readString();
        this.weight = in.readFloat();
        this.mark = in.readInt();
        this.color = in.readInt();
        long tmpDueDate = in.readLong();
        this.dueDate = tmpDueDate == -1 ? null : new Date(tmpDueDate);
    }

    public static final Parcelable.Creator<Assignment> CREATOR = new Parcelable.Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };
    */

    @Override
    public int compareTo(Assignment other){
        if(!isFinished() && other.isFinished()){
            return 1;
        }
        if(getDueDate().compareTo(other.getDueDate()) > 0)
           return 1;
        else if(getDueDate().compareTo(other.getDueDate()) < 0)
            return -1;
        else
            return 0;
    }
}
