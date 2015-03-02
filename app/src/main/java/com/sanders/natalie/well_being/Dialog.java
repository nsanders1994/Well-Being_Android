package com.sanders.natalie.well_being;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Natalie on 12/17/2014.
 */
public class Dialog extends Activity {
    int ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AlertDatabaseHandler dbHandler = new AlertDatabaseHandler(getApplicationContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Well-Being Survey");

        // set dialog message
        alertDialogBuilder
                .setMessage("\nAre you ready to take your survey?\n")
                .setNegativeButton("Delay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Update Delays
                        int delays = dbHandler.getDelays(ID) + 1;
                        dbHandler.setDelays(delays, ID);

                        if (delays < 2) {
                            Intent intent = new Intent(Dialog.this, PopupService.class);
                            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                            // Set alarm for 10 minutes
                            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    SystemClock.elapsedRealtime() + 60 * 1000, pendingIntent);
                            dialogInterface.cancel();
                            finish();
                        } else if (delays == 2) {
                            dbHandler.setDelays(0, ID);
                            dialogInterface.cancel();
                            finish();
                        }

                    }
                })
                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), String.valueOf(dbHandler.getDelays(ID)), Toast.LENGTH_SHORT).show();

                        if (dbHandler.getDelays(ID) > 0) {
                            dbHandler.setDelays(0, ID);
                            Intent i = new Intent(getApplicationContext(), DelayExplanation.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(i);
                            dialog.cancel();
                            finish();
                        } else {
                            dbHandler.setDelays(0, ID);
                            Intent i = new Intent(getApplicationContext(), Question1.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(i);
                            dialog.cancel();
                            finish();
                        }

                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        /*Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.token = findViewById(android.R.id.content).getWindowToken();
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);*/

        // show it
        alertDialog.show();

        // Vibrate phone
        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        // Sound alarm
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
