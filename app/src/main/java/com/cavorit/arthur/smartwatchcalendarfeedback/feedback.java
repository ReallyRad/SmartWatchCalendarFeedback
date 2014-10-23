package com.cavorit.arthur.smartwatchcalendarfeedback;

import android.content.ContentProvider;
import android.content.ContentUris;
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

import processing.core.*;

public class feedback extends PApplet {
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

    int swipe;

    Gestures g;      // create a gesture object

    public void setup() {
        // Run query
        size(240,240);
        g=new Gestures(100,50,this);    // iniate the gesture object first value is minimum swipe length in pixel and second is the diagonal offset allowed
        g.setSwipeUp("swipeUp");    // attach the function called swipeUp to the gesture of swiping upwards
        g.setSwipeDown("swipeDown");    // attach the function called swipeDown to the gesture of swiping downwards
        g.setSwipeLeft("swipeLeft");  // attach the function called swipeLeft to the gesture of swiping left
        g.setSwipeRight("swipeRight");  // attach the function called swipeRight to the gesture of swiping right
    }

    public void draw() {
        //background(255, 155, 155);
        //background(backgroundColor);    // draw the background with the color, this changes accordingly to what swipe is being carried out

        for (int i = 1; i < EVENT_PROJECTION.length; i++) {
            text(EVENT_PROJECTION[i],20 * i, 10);
        }

        String[] projection = new String[] { "calendar_id", "title", "description",
                "dtstart", "dtend", "eventLocation" };

        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/events"),
                projection,
                null,
                null,
                null);

        int j=0;

        while (cursor.moveToNext()) {
            j++;
            int calendar_id = 0;
            String title = "";
            String description = "";
            String dstart = "";
            String dend = "";

            calendar_id = cursor.getInt(0);
            title = cursor.getString(1);
            description = cursor.getString(2);
            dstart = cursor.getString(3);
            dend = cursor.getString(4);

            text(title, 20 + swipe, 20+ 20*j);
            text(description, 25 + swipe, 30 + 20*j);
        }
    }
/*
/*
Example code to show one way to work with gestures at Android devices.
Swipe left, right, up and down to call different functions that changes the background color.
Swipe settings such as how much directional swipe is allowed and minimum swipe length can be set easily in the constructor of the gesture class.

Tested on
osx
Processing 2.0a4
java version "1.6.0_29"
Android SDK tools 16
Samsung Galaxy Tab 10.1 Honeycomb 3.1

made by
david sjunnesson
david@tellart.com
Tellart.com
2012
*/


    // android touch event.
    public boolean surfaceTouchEvent(MotionEvent event) {
        // check what that was  triggered
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:    // ACTION_DOWN means we put our finger down on the screen
                g.setStartPos(new PVector(event.getX(), event.getY()));    // set our start position
                break;
            case MotionEvent.ACTION_UP:    // ACTION_UP means we pulled our finger away from the screen
                g.setEndPos(new PVector(event.getX(), event.getY()));    // set our end position of the gesture and calculate if it was a valid one
                break;
        }
        return super.surfaceTouchEvent(event);
    }

    // function that is called when we are swiping upwards
    void swipeUp() {
        println("a swipe up");
        //backgroundColor=color(100);
    }
    void swipeDown() {
        println("a swipe down");
        //backgroundColor=color(150);
    }
    void swipeLeft() {
        println("a swipe left");
        //backgroundColor=color(200);
    }
    void swipeRight() {
        println("a swipe right");
        //backgroundColor=color(250);
    }
}

/*
public class shake extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shake, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
*/
