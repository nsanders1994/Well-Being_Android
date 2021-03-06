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

import com.parse.ParseObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Finish extends Activity {
    boolean back_valid    = true;
    //public  Survey survey = new Survey();
    int     ans_ct = 0;
    int  [] ans;
    long [] tstamp;
    int     size;
    int     id;
    int     set_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish);

        final Button submitBttn = (Button) findViewById(R.id.bttnNext);
        final Button prevBttn   = (Button) findViewById(R.id.bttnBack);
        TextView progressTxt    = (TextView) findViewById(R.id.txtProgress);

        Intent curr_intent = getIntent();
        set_type = curr_intent.getIntExtra("SET_TYPE", 1);
        ans_ct = curr_intent.getIntExtra("CT", 0);
        ans    = curr_intent.getIntArrayExtra("ANS");
        tstamp = curr_intent.getLongArrayExtra("TSTAMP");
        id     = curr_intent.getIntExtra("ID", 0);
        size = ans.length;


        progressTxt.setText(ans_ct + "/" + size + " Questions Answered");
        //int n = survey.get_num_answered();
        //progressTxt.setText(n + "/4 Questions Answered");

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "The survey has been submitted.", Toast.LENGTH_SHORT).show();
                back_valid = false;

                // Submit survey to parse website
                sendToParse();

                // Return to home screen
                Intent intent = new Intent(Finish.this, Start.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        prevBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                if(set_type == 1) {
                    intent = new Intent(Finish.this, Questions_MultChoice.class);
                }
                else if(set_type == 2) {
                    intent = new Intent(Finish.this, Questions_Slider.class);
                }
                else {
                    intent = new Intent(Finish.this, Questions_MultChoice.class);
                }

                intent.putExtra("ANS", ans);
                intent.putExtra("TSTAMP", tstamp);
                intent.putExtra("SET_TYPE", set_type);

                setResult(3, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;

        if(set_type == 1) {
            intent = new Intent(Finish.this, Questions_MultChoice.class);
        }
        else if(set_type == 2) {
            intent = new Intent(Finish.this, Questions_Slider.class);
        }
        else {
            intent = new Intent(Finish.this, Questions_MultChoice.class);
        }

        intent.putExtra("ANS", ans);
        intent.putExtra("TSTAMP", tstamp);
        intent.putExtra("SET_TYPE", set_type);

        setResult(3, intent);
        finish();

    }

   /* public void createCVS(String filename) {
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

    }*/

    public void sendToParse() {

        ParseObject new_survey = new ParseObject("SurveyAnswers"); //Installation.id(this));
        new_survey.put("PID", Installation.id(this));

        for(int i = 0; i < size; i++) {
            Map<String, String> ques = new HashMap<String, String>();
            ques.put("value", String.valueOf(ans[i]));
            ques.put("timestamp", String.valueOf(tstamp[i]));

            new_survey.put("Q" + String.valueOf(i+1), ques);
        }

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
