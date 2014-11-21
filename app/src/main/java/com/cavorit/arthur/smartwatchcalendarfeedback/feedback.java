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
    //https://developer.android.com/guide/topics/providers/calendar-provider.html
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    String displayText;
    double dstart, dend;

    RadioButton[] radioButtons;
    int options;

    public void setup() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //mSensorManager.registerListener()
        //Run query
        smooth();
        options = 5;
        radioButtons = new RadioButton[options];
        for (int i = 0; i < options; i++) {
            int x = 30 + i * (width - 20) / options;
            int y = (int)(0.80*(float)height);
            radioButtons[i] = new RadioButton(x, y);
        }
        Ani.init(this);

        dstart = 0;
        dend = 0;

        displayText = getCalendarData();
    }

    public void draw() {
        int c1 = color(181, 214, 196);
        background(255);
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
    }

    public void mousePressed() {
        int clicked = -1;
        for (int i=0; i<options; i++) {
            if (pow(pow((mouseX-radioButtons[i].pos.x), 2) + pow((mouseY-radioButtons[i].pos.y), 2), 0.5f)<30) {
                clicked = i;
                //println("button ", i, " clicked");
                radioButtons[i].clicked();
            }
        }
        if (clicked >-1) { //if other radio button was clicked
            for (int i=0; i<options; i++) {
                if (clicked != i)  radioButtons[i].unselected();
                //println("button ", i, " unclicked");
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    float lala = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void keyPressed() {

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

    class RadioButton {
        public
        PVector pos;
        int outer_diam, inner_diam;
        boolean selected;
        int outer_fill, outer_stroke, inner_fill;

        RadioButton(float x, float y) {
            pos = new PVector(x,y);
            outer_diam = 30;
            inner_diam = 0;
            selected = false;

            outer_fill = color(148f,138f,138f);
            outer_stroke = color(203,246,172);
            inner_fill = color(248,245,211);
        }

        void draw() {
            strokeWeight(3);
            fill(outer_fill);
            stroke(outer_stroke);
            ellipse(pos.x, pos.y, outer_diam, outer_diam);
            fill(inner_fill);
            noStroke();
            if (selected) ellipse(pos.x+0.5f, pos.y+0.5f, inner_diam, inner_diam);
        }

        void clicked() {
            selected = true;
            Ani.to(this, 0.21f, "inner_diam", 17, Ani.BACK_OUT);
        }

        void unselected() {
            selected = false;
            //inner_diam = 0;
            Ani.to(this, 1.04f, "inner_diam", 0, Ani.BACK_OUT);
        }
    }
}

