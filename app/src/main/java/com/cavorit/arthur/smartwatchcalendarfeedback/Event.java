package com.cavorit.arthur.smartwatchcalendarfeedback;

import java.util.Comparator;
import java.util.Date;
import processing.core.*;

/**
 * Created by Badi on 12/2/2014.
 */
public class Event {
    public
    Integer calendarId;
    String title;
    String description;
    Date start;
    Date end;
    PApplet pApp;

    Event (Integer c, String t, String d, Date s, Date e, PApplet p) {
        calendarId = c;
        title = t;
        description = d;
        start = s;
        end = e;
        pApp = p;
    }

    void draw(int x) {
        pApp.fill(0);
        pApp.textSize(18);
        int origLength = title.length();
        while (pApp.textWidth(title)>210) {
           title = title.substring(0,title.length()-1);
        }
        if (title.length() < origLength) {
            title += "..";
        }
        pApp.text(title, x+10, 30);
        pApp.textSize(12);
        pApp.text(description, x+15, 50);
    }
}
