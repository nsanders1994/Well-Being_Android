package com.sanders.natalie.well_being;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Natalie on 12/17/2014.
 */
public class OnBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a repeating alarm to run IntentService that is impervious to killing the app or the phone going to sleep

        final AlertDatabaseHandler dbHandler = new AlertDatabaseHandler(context);

        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                0,
                new Intent(context, PopupService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Set time for survey pop-up
        Calendar cal = Calendar.getInstance();

        // Get alarm time
        int hr = dbHandler.getHr();
        int min = dbHandler.getMin();
        int curr_hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curr_min = Calendar.getInstance().get(Calendar.MINUTE);

        // If it's after the alarm time, schedule for next day
        if ( curr_hr > hr || curr_hr == hr && curr_min > min) {
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


        // Start alarm for a service which checks for updates
        PendingIntent pendingIntent2 = PendingIntent.getService(
                context,
                1,
                new Intent(context, DailyService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar cal2 = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        alarmManager2.setInexactRepeating(
                AlarmManager.RTC,
                cal2.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent2);
    }
}
