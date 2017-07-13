package com.spenler.peter.classmanager;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peter on 5/10/17.
 */

public class Assignment implements Parcelable {

    private String name, course;
    private int weight, mark, color;
    private Date dueDate;

    public Assignment(String name, int weight, Date dueDate, String course, int color){
        this.name = name;
        this.weight = weight;
        this.dueDate = dueDate;
        this.course = course;
        this.color = color;
    }

    public int setMark(int mark){
        this.mark = mark;
        return mark;
    }

    public String getName(){
        return name;
    }

    public int getWeight(){
        return weight;
    }

    public Date getDueDate(){
        return dueDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.course);
        dest.writeInt(this.weight);
        dest.writeInt(this.mark);
        dest.writeInt(this.color);
        dest.writeLong(this.dueDate != null ? this.dueDate.getTime() : -1);
    }

    protected Assignment(Parcel in) {
        this.name = in.readString();
        this.course = in.readString();
        this.weight = in.readInt();
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
}
