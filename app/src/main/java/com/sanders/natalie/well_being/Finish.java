package com.sanders.natalie.well_being;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Finish extends Activity {
    boolean back_valid = true;
    public Survey survey = new Survey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        final Button submitBttn = (Button) findViewById(R.id.bttnSubmit);
        final Button prevBttn   = (Button) findViewById(R.id.bttnBack);
        TextView progressTxt    = (TextView) findViewById(R.id.txtProgress);

        Intent curr_intent = getIntent();
        Survey s = curr_intent.getParcelableExtra("SURVEY");
        if(s != null) { survey = s; }

        int n = survey.get_num_answered();
        progressTxt.setText(n + "/2 Questions Answered");

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "The survey has been submitted.", Toast.LENGTH_SHORT).show();
                back_valid = false;

                //TODO Send survey data to csv file on phone
                DateFormat df = new SimpleDateFormat("MM-dd-yy_HH:mm");
                Date dateobj = new Date();

                createCVS(df.format(dateobj) + ".csv");
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);            }
        });

                //TODO Submit survey to parse website



        prevBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("SURVEY", survey);
                if(intent != null && back_valid) {
                    finish();
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
