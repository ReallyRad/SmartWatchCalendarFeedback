package com.cavorit.arthur.smartwatchcalendarfeedback;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import de.looksgood.ani.*;
import de.looksgood.ani.easing.*;

import processing.core.*;

public class Feedback extends PApplet implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    float[] accelData;

    RadioButton[] radioButtons;
    int options;

    ArrayList<Event> events;
    Event[] eventsDisplay;
    Integer eventsIndex;
    int swipe;

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

        //get calendar data and current index
        events = getEvents();
        eventsIndex = 0;
        double time = System.currentTimeMillis();
        int i = 0;
        while ((eventsIndex == 0) && (i < events.size())) { // get index of most recent event
            if (events.get(i).start.getTime() > time) eventsIndex = i-1;
            i++;
        }

        //swipe stuff
        gestureManager = new Gestures(20, 45, this);
        gestureManager.setAction(0, "swipeLeft");
        gestureManager.setAction(2, "swipeRight");
        beganSwipe = false;
        swipe = 0;
        eventsDisplay = new Event[3];
        eventsDisplay[0] = events.get(eventsIndex-1);
        eventsDisplay[1] = events.get(eventsIndex);
        eventsDisplay[2] = events.get(eventsIndex+1);
    }

    public void draw() {
        background(255);
        int c1 = color(181, 214, 196);
        int c2 = color(195, 96, 93);
        //setGradient(0, 0, width, height, c1, c2);
        for (int i=0; i<options; i++) {
            radioButtons[i].draw();
        }
        //text(String.valueOf(accelData[0])+ " " + String.valueOf(accelData[1])+ " " + String.valueOf(accelData[2]), 15, 65);

        for (int i=-4; i<5; i++){
            events.get(eventsIndex+i).draw(swipe+240*i);
        }


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

    //public Ani(java.lang.Object theTarget, float theDuration, java.lang.String theFieldName, float theEnd) { /* compiled code */ }
    //public Ani(java.lang.Object theTarget, float theDuration, float theDelay, java.lang.String theFieldName, float theEnd) { /* compiled code */ }
    //public Ani(java.lang.Object theTarget, float theDuration, java.lang.String theFieldName, float theEnd, de.looksgood.ani.easing.Easing theEasing) { /* compiled code */ }
    //public Ani(java.lang.Object theTarget, float theDuration, float theDelay, java.lang.String theFieldName, float theEnd, de.looksgood.ani.easing.Easing theEasing) { /* compiled code */ }
    //public Ani(java.lang.Object theTarget, float theDuration, java.lang.String theFieldName, float theEnd, de.looksgood.ani.easing.Easing theEasing, java.lang.String theCallback) { /* compiled code */ }
    //public Ani(java.lang.Object theTarget, float theDuration, float theDelay, java.lang.String theFieldName, float theEnd, de.looksgood.ani.easing.Easing theEasing, java.lang.String theCallback) { /* compiled code */ }

    public void swipeLeft(){
        //Ani.to(this, 1.51f, "swipe", -240, Ani.BACK_OUT, "onEnd:swipeRightEnded");
        //eventsDisplay[2]=events(eventsIndex+1);

        Ani aniSwipeLeft = new Ani(this, 0.7f, 0f, "swipe", swipe-240, Ani.LINEAR, "onEnd:swipeLeftEnded");
        aniSwipeLeft.start();
    }
    public void swipeRight(){
        //Ani.to(this, 1.51f, "swipe", 240, Ani.BACK_OUT, "onDelayEnd:swipeLeftEnded");
        Ani aniSwipeRight = new Ani(this, 0.7f, 0f, "swipe", swipe+240, Ani.LINEAR, "onDelayEnded:swipeRightEnded");
        aniSwipeRight.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelData = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void swipeLeftEnded() {
        //Ani.to(this, 0f, "swipe", 0, Ani.LINEAR);
        eventsIndex++;
        swipe = 0;
    }

    void swipeRightEnded(){
        //Ani.to(this, 0f, "swipe", 0, Ani.LINEAR);
        eventsIndex--;
        swipe = 0;
    }

    void setGradient(int x, int y, float w, float h, int c1, int c2) {
        noFill();
        for (int i = y; i <= y+h; i++) {
            float inter = map(i, y, y+h, 0, 1);
            stroke(lerpColor(c1, c2, inter));
            line(x, i, x+w, i);
        }
    }

    public class CustomComparator implements Comparator<Event> {
        @Override
        public int compare(Event e1, Event e2) {
            return e1.start.compareTo(e2.end);
        }
    }

    void getIndexFromDate(){

    }

    ArrayList<Event> getEvents() {
        //https://developer.android.com/guide/topics/providers/calendar-provider.html
        // Projection array. Creating indices for this array instead of doing
        // dynamic lookups improves performance.
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        ArrayList<Event> events = new ArrayList<Event>();

        String[] projection = new String[]{"calendar_id", "title", "description",
                "dtstart", "dtend", "eventLocation"};

        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/events"),
                projection,
                null,
                null,
                null);

        for (int j = 0; cursor.moveToNext(); j++){
            int calendar_id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);

            double dstart = cursor.getDouble(3);
            double dend = cursor.getDouble(4);
            double time = System.currentTimeMillis();

            events.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    new Date(cursor.getLong(3)), new Date(cursor.getLong(4)), this));
                    //calendar id, title, description, start date, end date, PApplet
        }

        Collections.sort(events, new CustomComparator());
        return events;
    }

}