package com.sanders.natalie.well_being;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Natalie on 2/16/2015.
 */
public class Question1_Slider extends Activity {

    Survey survey = new Survey();
    SeekBar seekBar1;
    SeekBar seekBar2;
    SeekBar seekBar3;
    SeekBar seekBar4;
    boolean back_valid = true;
    AlertDatabaseHandler alertHandler;
    SurveyDatabaseHandler surveyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question1_slider);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z6S6iux9qyLGcCsAE3vuRvhHWDwFelxzT2nSqKWc", "boXMTOaotk2HgGpxFLdNNPFw1d7WwB7c3G4nPHak");

        alertHandler = new AlertDatabaseHandler(getApplicationContext());
        surveyHandler = new SurveyDatabaseHandler(getApplicationContext());


        final Button submitBttn = (Button) findViewById(R.id.bttnSubmit);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar) findViewById(R.id.seekBar4);

        // Start system alarm for the survey popup if not already started
        start_DialogAlarm();

        // Start background service to check for updated popup times
        start_UpdatesService();

        seekBar1.setProgress(0);
        seekBar1.setMax(5);

        seekBar2.setProgress(0);
        seekBar2.setMax(5);

        seekBar3.setProgress(0);
        seekBar3.setMax(5);

        seekBar4.setProgress(0);
        seekBar4.setMax(5);

        if(!surveyHandler.isEmpty()) {

            Toast.makeText(Question1_Slider.this, "it's not empty", Toast.LENGTH_SHORT);
            survey = surveyHandler.getSurvey();
            seekBar1.incrementProgressBy(survey.get_q1());
            seekBar2.incrementProgressBy(survey.get_q2());
            seekBar3.incrementProgressBy(survey.get_q3());
            seekBar4.incrementProgressBy(survey.get_q4());
        }

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                survey.set_q1(i);
                survey.set_q1_tstamp();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                survey.set_q2(i);
                survey.set_q2_tstamp();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                survey.set_q3(i);
                survey.set_q3_tstamp();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                survey.set_q4(i);
                survey.set_q4_tstamp();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(back_valid) {
                    Toast.makeText(getApplicationContext(), "The survey has been submitted.", Toast.LENGTH_SHORT).show();
                    back_valid = false;

                    // Send survey data to csv file on phone
                    DateFormat df = new SimpleDateFormat("MM-dd-yy_HH:mm");
                    Date dateobj = new Date();

                    createCVS(df.format(dateobj) + ".csv");

                    // Submit survey to parse website
                    sendToParse();

                    surveyHandler.putSurvey(survey);
                    // TODO Return to start screen of app as result 4, in start screen activity go to home screen on returned 4
                    // Return to home screen
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
                else {
                    Toast.makeText(getApplicationContext(), "This survey has already been submitted.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createCVS(String filename) {
        try {
            File root = Environment.getExternalStorageDirectory().getAbsoluteFile();
            File folder = new File(root, "Wellbeing-Surveys");
            boolean success = true;

            if(!folder.exists()) {
                success = folder.mkdir();
            }

            if(success) {
                File csv = new File(folder, filename);

                FileWriter fileWriter = new FileWriter(csv);

                fileWriter.append("QuestionNumber");
                fileWriter.append(",");
                fileWriter.append("Value");
                fileWriter.append(",");
                fileWriter.append("UnixTimeStamp");
                fileWriter.append("\n");

                fileWriter.append("Q1.1");
                fileWriter.append(",");
                fileWriter.append(String.valueOf(survey.get_q1()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(survey.get_q1_tstamp()));
                fileWriter.append("\n");

                fileWriter.append("Q1.2");
                fileWriter.append(",");
                fileWriter.append(String.valueOf(survey.get_q2()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(survey.get_q2_tstamp()));
                fileWriter.append("\n");

                fileWriter.close();
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void sendToParse() {

        Map<String, String> Q1 = new HashMap<String, String>();
        Map<String, String> Q2 = new HashMap<String, String>();
        Map<String, String> Q3 = new HashMap<String, String>();
        Map<String, String> Q4 = new HashMap<String, String>();

        Q1.put("value", String.valueOf(survey.get_q1()));
        Q2.put("value", String.valueOf(survey.get_q2()));
        Q3.put("value", String.valueOf(survey.get_q3()));
        Q4.put("value", String.valueOf(survey.get_q4()));

        Q1.put("timestamp", String.valueOf(survey.get_q1_tstamp()));
        Q2.put("timestamp", String.valueOf(survey.get_q2_tstamp()));
        Q3.put("timestamp", String.valueOf(survey.get_q3_tstamp()));
        Q4.put("timestamp", String.valueOf(survey.get_q4_tstamp()));

        ParseObject new_survey = new ParseObject("Survey"); //Installation.id(this));
        new_survey.put("PID", Installation.id(this));
        new_survey.put("Q1", Q1);
        new_survey.put("Q2", Q2);
        new_survey.put("Q3", Q3);
        new_survey.put("Q4", Q4);
        new_survey.saveInBackground();
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
        if(alertHandler.isInitialized() > 0) {
            alertHandler.setHr(0);
            alertHandler.setMin(0);
        }
        else {
            alertHandler.init();
            alertHandler.setHr(0);
            alertHandler.setMin(0);
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
        int hr = alertHandler.getHr();
        int min = alertHandler.getMin();
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

}
