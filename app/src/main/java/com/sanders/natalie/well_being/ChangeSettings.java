package com.sanders.natalie.well_being;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Natalie on 2/8/2015.
 */
public class ChangeSettings extends Activity {
    /* Allows users to change the time of the survey popup.
       Activated from the settings button in the Start activity
    */
    TimePickerDialog timePickerDialog;

    final Calendar cal = Calendar.getInstance();
    final int hr       = cal.get(Calendar.HOUR_OF_DAY);
    final int min      = cal.get(Calendar.MINUTE);

    // Initialize new times
    int new_hr = hr;
    int new_min = min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AlertDatabaseHandler dbHandler = new AlertDatabaseHandler(getApplicationContext());

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                new_hr = i;
                new_min = i2;
            }
        }, hr, min, false);

        timePickerDialog.setTitle("Change Pop-up Time:");
        timePickerDialog.setCanceledOnTouchOutside(true);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timePickerDialog.cancel();
                        finish();
                    }
                });
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHandler.setHr(new_hr);
                        dbHandler.setMin(new_min);
                        Toast.makeText(
                                getApplicationContext(),
                                "Pop-up time set for " + new_hr + ":" + new_min,
                                Toast.LENGTH_SHORT).show();

                    }
                });

        timePickerDialog.show();
    }
}
