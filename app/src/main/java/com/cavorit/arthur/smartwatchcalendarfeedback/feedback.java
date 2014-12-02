package com.cavorit.arthur.smartwatchcalendarfeedback;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.database.Cursor;
import android.content.ContentResolver;
import android.net.Uri;
import android.accounts.AbstractAccountAuthenticator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import java.util.Date;
import de.looksgood.ani.*;
import de.looksgood.ani.easing.*;
import processing.core.*;

public class feedback extends PApplet implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    float[] accelData;

    RadioButton[] radioButtons;
    int options;

    String displayText;

    Gestures gestureManager;
    boolean beganSwipe;

    public void setup() {
        //Processing stuff
        frameRate(60);
        smooth();

        //sensor stuff
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        //radio buttons stuff
        options = 5;
        radioButtons = new RadioButton[options];
        for (int i = 0; i < options; i++) {
            int x = 30 + i * (width - 20) / options;
            int y = (int)(0.80*(float)height);
            radioButtons[i] = new RadioButton(x, y, this);
        }
        Ani.init(this);

        //get calendar data
        displayText = getCalendarData();

        //swipe stuff
        gestureManager = new Gestures(20, 45, this);
        gestureManager.setAction(0, "swipeLeft");
        gestureManager.setAction(2, "swipeRight");
        beganSwipe = false;
    }

    public void swipeLeft(){
        println("swipe left!");
    };
    public void swipeRight(){
        println("swipe right");
    };

    public void draw() {
        background(255);
        int c1 = color(181, 214, 196);
        int c2 = color(195, 96, 93);
        //setGradient(0, 0, width, height, c1, c2);
        for (int i=0; i<options; i++) {
            radioButtons[i].draw();
        }
        fill(0);
        textSize(24);
        text(displayText.split("\n")[0], 15, 30);
        textSize(12);
        text(displayText.split("\n")[1], 15, 50);
        text(String.valueOf(accelData[0])+ " " + String.valueOf(accelData[1])+ " " + String.valueOf(accelData[2]), 15, 65);
                
    }

    public void mousePressed() { //handle radiobuttons and trigger swipes
        int clicked = -1;
        for (int i=0; i<options; i++) {
            if (pow(pow((mouseX-radioButtons[i].pos.x), 2) + pow((mouseY-radioButtons[i].pos.y), 2), 0.5f)<30) {
                clicked = i;
                radioButtons[i].clicked();
            }
        }
        if (clicked >-1) { //if some radio button was clicked
            for (int i=0; i<options; i++) {
                if (clicked != i)  radioButtons[i].unselected();
            }
        }
        else { //if no radio button was clicked, check if swipe
            gestureManager.setStartPos(new PVector(mouseX, mouseY));
            beganSwipe = true;
        }
    }

    public void mouseReleased() {
        if (beganSwipe) {
            gestureManager.setEndPos(new PVector(mouseX, mouseY));
            beganSwipe = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelData = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void setGradient(int x, int y, float w, float h, int c1, int c2) {
        noFill();
        for (int i = y; i <= y+h; i++) {
            float inter = map(i, y, y+h, 0, 1);
            stroke(lerpColor(c1, c2, inter));
            line(x, i, x+w, i);
        }
    }

    String getCalendarData() {
        //https://developer.android.com/guide/topics/providers/calendar-provider.html
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        String[] EVENT_PROJECTION = new String[]{
                Calendars._ID,                           // 0
                Calendars.ACCOUNT_NAME,                  // 1
                Calendars.CALENDAR_DISPLAY_NAME,         // 2
                Calendars.OWNER_ACCOUNT                  // 3
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        double dstart, dend;

        String returnString = "no current events";
        for (int i = 1; i < EVENT_PROJECTION.length; i++) {
            //text(EVENT_PROJECTION[i], 20 * i, 10);
        }

        String[] projection = new String[]{"calendar_id", "title", "description",
                "dtstart", "dtend", "eventLocation"};

        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/events"),
                projection,
                null,
                null,
                null);

        int j = 0;

        while (cursor.moveToNext()) {
            j++;
            int calendar_id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);

            dstart = cursor.getDouble(3);
            dend = cursor.getDouble(4);
            double time = System.currentTimeMillis();

            if ((time < dend) && (time > dstart)) {//if event is currently happening
                returnString = title + "\n " +  description;
            }
        }

        return returnString;
    }
}

