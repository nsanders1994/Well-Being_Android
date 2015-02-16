package com.sanders.natalie.well_being;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Calendar;

/**
 * Created by Natalie on 2/15/2015.
 */
public class DailyService extends IntentService {

    AlertDatabaseHandler dbHandler;

    public DailyService() {
        super("Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHandler = new AlertDatabaseHandler(getApplicationContext());
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z6S6iux9qyLGcCsAE3vuRvhHWDwFelxzT2nSqKWc", "boXMTOaotk2HgGpxFLdNNPFw1d7WwB7c3G4nPHak");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Updates");
        query.orderByDescending("updatedAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Updates", "The getFirst request failed.");
                } else {
                    int hr = object.getInt("Hour");
                    int min = object.getInt("Minute");
                    reset_DialogAlarm(hr, min);
                    Toast.makeText(getApplicationContext(), "New time = " + hr + ":" + min, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void reset_DialogAlarm(int hr, int min) {

        dbHandler.setHr(hr);
        dbHandler.setMin(min);

        // Alarm is reset at new time
        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                0,
                new Intent(DailyService.this, PopupService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set new time
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

        // Set alarm for survey pop-up
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);
    }
}
