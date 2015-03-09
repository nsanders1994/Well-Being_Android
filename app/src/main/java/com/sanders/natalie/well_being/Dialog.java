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

        // Get survey ID from caller intent
        Intent caller = getIntent();
        ID = caller.getIntExtra("ID", 1);

        // Initialize Database
        final AlertDatabaseHandler dbHandler = new AlertDatabaseHandler(getApplicationContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title
        String title = dbHandler.getName(ID);
        alertDialogBuilder.setTitle(title + " Survey");

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
                            // Create new intent with ID extra
                            Intent intent = new Intent(Dialog.this, PopupService.class);
                            intent.putExtra("ID", ID);
                            PendingIntent pendingIntent = PendingIntent.getService(
                                    getApplicationContext(),
                                    ID,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

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
                            i.putExtra("ID", ID);

                            startActivity(i);
                            dialog.cancel();
                            finish();

                        } else {
                            Class QActivity;
                            dbHandler.setDelays(0, ID);
                            int set_type = dbHandler.getSetType(ID);

                            if(set_type == 1) {
                                QActivity = Questions.class;
                            }
                            else if(set_type == 2) {
                                QActivity = Question1_Slider.class;
                            }
                            else {
                                QActivity = Questions.class;
                            }

                            Intent i = new Intent(getApplicationContext(), QActivity);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.putExtra("ID", ID);

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
