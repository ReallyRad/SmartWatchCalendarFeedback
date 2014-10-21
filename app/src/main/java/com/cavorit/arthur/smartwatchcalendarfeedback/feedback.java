package com.cavorit.arthur.smartwatchcalendarfeedback;

import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.database.Cursor;
import android.content.ContentResolver;
import android.net.Uri;
import android.accounts.AbstractAccountAuthenticator;
import android.view.Menu;
import android.view.MenuItem;

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

    public void setup() {
        // Run query

    }

    public void draw() {
        background(255, 155, 155);
        for (int i = 1; i < EVENT_PROJECTION.length; i++) {
            text(EVENT_PROJECTION[i],20 * i, 10);
        }

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"sampleuser@gmail.com", "com.google",
                "sampleuser@gmail.com"};
        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        // Use the cursor to step through the returned records
        int i = 0;
        while (cur.moveToNext()) {
            i++;
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            text(displayName,0,200*mouseX);
            text(accountName, 20, 400*mouseX);
            text(ownerName, 40, 600*mouseX);
            // Do something with the values...

        }

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
