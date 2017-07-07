package com.spenler.peter.classmanager;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peter on 5/10/17.
 */

public class Assignment {

    private String name;
    private int weight, mark, course;
    private Color color;
    private Date dueDate;

    public Assignment(String name, int weight, Date dueDate, int course, Color color){
        this.name = name;
        this.weight = weight;
        this.dueDate = dueDate;
        this.course = course;
        this.color = color;
    }
}
