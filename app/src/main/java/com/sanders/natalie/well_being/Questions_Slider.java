package com.sanders.natalie.well_being;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Natalie on 2/16/2015.
 */
public class Questions_Slider extends Activity {

    Button   nextBttn;
    Button   prevBttn;
    SeekBar  seekBar1;
    SeekBar  seekBar2;
    SeekBar  seekBar3;
    SeekBar  seekBar4;
    SeekBar  [] seekBars   = {seekBar1, seekBar2, seekBar3, seekBar4};
    int      [] seekBarIDs = {R.id.seekBar1, R.id.seekBar2, R.id.seekBar3, R.id.seekBar4};

    TextView baseQTxt;
    TextView subQTxt1;
    TextView subQTxt2;
    TextView subQTxt3;
    TextView subQTxt4;
    TextView [] subQtxt    = {subQTxt1, subQTxt2, subQTxt3, subQTxt4};
    int      [] subQtxtIDs = {R.id.txtSubQ1, R.id.txtSubQ2, R.id.txtSubQ3, R.id.txtSubQ4};

    SurveyDatabaseHandler dbHandler;
    SurveyDatabaseHandler surveyHandler;

    private int          vwNo = 0;
    private int          maxVw;
    private int          ID;
    private String       baseQ;
    private List<String> subQ;
    private long []      tstamp;
    private int  []      ans;
    private int          size;
    private int          ans_ct  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new SurveyDatabaseHandler(getApplicationContext());
        surveyHandler = new SurveyDatabaseHandler(getApplicationContext());

        // Retrieve survey's id
        Intent curr_intent = getIntent();
        ID = curr_intent.getIntExtra("ID", 1);

        // Initialize variables
        baseQ  = dbHandler.getBaseQues(ID);
        subQ   = dbHandler.getSubQuesList(ID);
        size   = subQ.size();
        ans    = new int[size];
        tstamp = new long[size];
        maxVw  = size % 4;


        // Initialize Layout
        nextBttn = (Button)   findViewById(R.id.bttnNext);
        setContentView(R.layout.activity_slider);

        for(int i = 0; i < 4; i++) {
            // Set SeekBar
            seekBars[i] = (SeekBar) findViewById(seekBarIDs[0]);
            seekBars[i].setProgress(0);
            seekBars[i].setMax(5);
            seekBars[i].incrementProgressBy(ans[vwNo + i]);

            // Set TextView
            subQtxt[i]  = (TextView) findViewById(subQtxtIDs[0]);
            subQtxt[vwNo*4 + i].setText(subQ.get(vwNo*4 + i));   
        }

        baseQTxt = (TextView) findViewById(R.id.txtBaseQ);
        baseQTxt.setText(baseQ);

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
                if(vwNo == maxVw) {
                    Intent intent = new Intent(Questions_Slider.this, Finish.class);
                    intent.putExtra("ANS", ans);
                    intent.putExtra("TSTAMP", tstamp);
                    intent.putExtra("CT", ans_ct);
                    intent.putExtra("ID", ID);

                    startActivityForResult(intent, 3);
                }
                else {
                    vwNo++;

                    for(int i = 0; i < 4; i++) {
                        // Set SeekBar
                        seekBars[i] = (SeekBar) findViewById(seekBarIDs[0]);
                        seekBars[i].setProgress(0);
                        seekBars[i].setMax(5);

                        // Set TextView
                        subQtxt[vwNo + i].setText(subQ.get(vwNo + i));
                    }


                }


            }
        });
    }

    public void set_tstamp(int qNo) {
        if(ans[qNo] != 0) {
            tstamp[qNo] = System.currentTimeMillis() / 1000L;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
    }
}
