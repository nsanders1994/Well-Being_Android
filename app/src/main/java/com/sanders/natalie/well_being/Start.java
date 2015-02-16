package com.sanders.natalie.well_being;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;


public class Start extends Activity {
    AlertDatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dbHandler = new AlertDatabaseHandler(getApplicationContext());
        TableRow R1 = (TableRow) findViewById(R.id.tableRow1);
        TableRow R2 = (TableRow) findViewById(R.id.tableRow2);

        R1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The alarm time before reset
                int prevAlarm_hr = dbHandler.getHr();
                int prevAlarm_min = dbHandler.getMin();

                // Administrator changes time for next day
                Intent i = new Intent(Start.this, ChangeSettings.class);
                startActivity(i);

                // Dialog Alarm is reset
                reset_DialogAlarm(prevAlarm_hr, prevAlarm_min);
            }
        });

        R2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this, Question1.class);
                startActivity(i);
            }
        });

        // Start system alarm for the survey popup if not already started
        start_DialogAlarm();

        // Start background service to check for updated popup times
        start_UpdatesService();
    }
    public void start_UpdatesService() {

        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                1,
                new Intent(getApplicationContext(), DailyService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);
    }
    public void start_DialogAlarm() {

        // Database handler for accessing alarm time
        if(dbHandler.isInitialized() > 0) {
            dbHandler.setHr(0);
            dbHandler.setMin(0);
        }
        else {
            dbHandler.init();
            dbHandler.setHr(0);
            dbHandler.setMin(0);
        }

        // Alarm will trigger the pop-up dialog
        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), PopupService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set time for survey pop-up
        Calendar cal = Calendar.getInstance();

        // Get alarm time
        int hr = dbHandler.getHr();
        int min = dbHandler.getMin();
        int curr_hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curr_min = Calendar.getInstance().get(Calendar.MINUTE);

        // If it's after the alarm time, schedule for next day
        Toast.makeText(getApplicationContext(), curr_hr + ":" + curr_min + " " + hr + ":" + min, Toast.LENGTH_SHORT).show();
        if ( curr_hr > hr || curr_hr == hr && curr_min > min) {
            Toast.makeText(getApplicationContext(), "Next Day", Toast.LENGTH_SHORT).show();
            cal.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }

        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

        // Set alarm for survey pop-up to go off at default of 8:00 AM
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    public void reset_DialogAlarm(int p_hr, int p_min) {

        // Database handler for accessing alarm time
        if(dbHandler.isInitialized() > 0) {
            dbHandler.setHr(4);
            dbHandler.setMin(17);
        }
        else {
            dbHandler.init();
            dbHandler.setHr(12);
            dbHandler.setMin(0);
        }

        // Alarm is reset at new time for the next day
        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), PopupService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Get alarm time
        int hr = dbHandler.getHr();
        int min = dbHandler.getMin();

        // Set time for next day
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

        // Set alarm for survey pop-up to go off at default of 8:00 AM
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);

        // If the popup hasn't already come today, reset today's alarm for the previous time
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < p_hr &&
           Calendar.getInstance().get(Calendar.MINUTE) <= p_min)
        {

            PendingIntent pendingIntent2 = PendingIntent.getService(
                    getApplicationContext(),
                    1,
                    new Intent(Start.this, PopupService.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.HOUR_OF_DAY, hr);
            cal2.set(Calendar.MINUTE, min);
            cal2.set(Calendar.SECOND, 0);

            alarmManager2.set(
                    AlarmManager.RTC_WAKEUP,
                    cal2.getTimeInMillis(),
                    pendingIntent2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // The alarm time before reset
            int prevAlarm_hr = dbHandler.getHr();
            int prevAlarm_min = dbHandler.getMin();

            // Administrator changes time for next day
            Intent i = new Intent(this, ChangeSettings.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);

            // Dialog Alarm is reset
            reset_DialogAlarm(prevAlarm_hr, prevAlarm_min);
        }
        return super.onOptionsItemSelected(item);
    }

    /*private class SurveyAdapter extends ArrayAdapter<Note> {
        public NoteAdapter() {

            super (Start.this, R.layout.listview_note, Notes);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_note, parent, false);
            Note currentNote = Notes.get(position);

            TextView label = (TextView) view.findViewById(R.id.currentLabel);
            label.setText(currentNote.get_label());

            return view;
        }
    }

    private void populateList() {
        adapter = new NoteAdapter();
        noteListView.setAdapter(adapter);
    }*/
}
