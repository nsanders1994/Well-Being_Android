package com.sanders.natalie.well_being;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Finish extends Activity {
    boolean back_valid = true;
    public Survey survey = new Survey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z6S6iux9qyLGcCsAE3vuRvhHWDwFelxzT2nSqKWc", "boXMTOaotk2HgGpxFLdNNPFw1d7WwB7c3G4nPHak");

        setContentView(R.layout.activity_finish);

        final Button submitBttn = (Button) findViewById(R.id.bttnSubmit);
        final Button prevBttn   = (Button) findViewById(R.id.bttnBack);
        TextView progressTxt    = (TextView) findViewById(R.id.txtProgress);

        Intent curr_intent = getIntent();
        Survey s = curr_intent.getParcelableExtra("SURVEY");
        if(s != null) { survey = s; }

        int n = survey.get_num_answered();
        progressTxt.setText(n + "/4 Questions Answered");

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

        prevBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Finish.this, Question2.class);
                intent.putExtra("SURVEY", survey);
                if(intent != null && back_valid) {
                    setResult(3, intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "This survey has already been submitted.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable the phone's back button if the survey's been submitted
        if(back_valid) {
            finish();
        }
        else {
            // TODO Return to start screen
            Toast.makeText(getApplicationContext(), "This survey has already been submitted.", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
