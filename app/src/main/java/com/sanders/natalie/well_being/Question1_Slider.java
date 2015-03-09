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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Natalie on 2/16/2015.
 */
public class Question1_Slider extends Activity {

    Survey survey = new Survey();

    Button  nextBttn;
    SeekBar  seekBar1;
    SeekBar  seekBar2;
    SeekBar  seekBar3;
    SeekBar  seekBar4;

    TextView baseQTxt;
    TextView subQTxt1;
    TextView subQTxt2;
    TextView subQTxt3;
    TextView subQTxt4;
    TextView [] subQ = {subQTxt1, subQTxt2, subQTxt3, subQTxt4};

    boolean back_valid = true;
    AlertDatabaseHandler dbHandler;
    SurveyDatabaseHandler surveyHandler;

    private int       id;
    private String    question;
    private List<String> sub_ques; // {getString(R.string.Q1_1)
    private long   [] tstamp;
    private int    [] ans;
    private int       size;
    private int       ans_ct  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new AlertDatabaseHandler(getApplicationContext());
        surveyHandler = new SurveyDatabaseHandler(getApplicationContext());

        // Retrieve survey's id
        Intent curr_intent = getIntent();
        id = curr_intent.getIntExtra("ID", 0);

        // Initialize variables
        question = dbHandler.getBaseQues(id);
        sub_ques = dbHandler.getSubQuesArray(id);
        size     = sub_ques.size();
        ans      = new int[size];
        tstamp   = new long[size];

        // Initialize Layout
        nextBttn = (Button)   findViewById(R.id.bttnNext);
        seekBar1 = (SeekBar)  findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar)  findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar)  findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar)  findViewById(R.id.seekBar4);
        subQTxt1 = (TextView) findViewById(R.id.txtSubQ1);
        subQTxt2 = (TextView) findViewById(R.id.txtSubQ2);
        subQTxt3 = (TextView) findViewById(R.id.txtSubQ3);
        subQTxt4 = (TextView) findViewById(R.id.txtSubQ4);
        baseQTxt = (TextView) findViewById(R.id.txtBaseQ);

        // Initialize View
        setContentView(R.layout.activity_question1_slider);
        baseQTxt.setText(question);
        for(int i = 0; i < subQ.length; i++) {
            subQ[i].setText(sub_ques.get(i));
        }

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
                ans[0] = 1;
                set_tstamp(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ans[1] = 2;
                set_tstamp(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ans[2] = 3;
                set_tstamp(2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ans[3] = 4;
                set_tstamp(3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question1_Slider.this, Finish.class);
                intent.putExtra("ANS", ans);
                intent.putExtra("TSTAMP", tstamp);
                intent.putExtra("CT", ans_ct);
                intent.putExtra("ID", id);

                startActivityForResult(intent, 5);

            }
        });
    }

    public void set_tstamp(int qNo) {
        if(ans[qNo] != 0) {
            tstamp[qNo] = System.currentTimeMillis() / 1000L;
        }
    }
}
