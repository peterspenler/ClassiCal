package com.spenler.peter.classmanager;

import android.graphics.Color;

import java.util.Date;

/**
 * Created by peter on 5/10/17.
 */

public class Lecture {
    private Date start, end;
    private Color color;

    public Lecture(Date start, Date end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }
}
