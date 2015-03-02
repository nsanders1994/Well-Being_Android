package com.sanders.natalie.well_being;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

/*
 * Created by Natalie on 2/16/2015.
 */

public class Questions extends Activity {
    private Button      nextBttn;
    private Button      prevBttn;
    private TextView    subQtxt;
    private TextView    baseQtxt;
    private TableRow [] rows = new TableRow[5];
    private int         vwNo = 0;

    private int       id;
    private String    question;
    private String [] sub_ques; // {getString(R.string.Q1_1)
    private long   [] tstamp;
    private int    [] ans;
    private int       size;
    private int       ans_ct  = 0;
    private boolean   new_ans = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDatabaseHandler dbHandler = new AlertDatabaseHandler(getApplicationContext());

        // Retrieve survey's id
        Intent curr_intent = getIntent();
        id = curr_intent.getIntExtra("ID", 0);

        // Initialize variables
        question = dbHandler.getBaseQues(id);
        sub_ques = dbHandler.getSubQuesArray(id);
        size     = sub_ques.length;
        ans      = new int[size];
        tstamp   = new long[size];

        // Initialize Buttons
        nextBttn = (Button)   findViewById(R.id.bttnNext);
        prevBttn = (Button)   findViewById(R.id.bttnBack);
        subQtxt  = (TextView) findViewById(R.id.txtSubQues);
        baseQtxt = (TextView) findViewById(R.id.txtBaseQues);
        rows[0]  = (TableRow) findViewById(R.id.tableRow1);
        rows[1]  = (TableRow) findViewById(R.id.tableRow2);
        rows[2]  = (TableRow) findViewById(R.id.tableRow3);
        rows[3]  = (TableRow) findViewById(R.id.tableRow4);
        rows[4]  = (TableRow) findViewById(R.id.tableRow5);

        // Initialize View
        setContentView(R.layout.activity_question1);
        subQtxt.setText(sub_ques[vwNo]);
        baseQtxt.setText(question);

        // On the event of the user clicking 'next'
        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(vwNo + 1 == size)
            {
                Intent intent = new Intent(Questions.this, Finish.class);
                if(new_ans) {
                    set_tstamp();
                }
                update_count();
                intent.putExtra("ANS", ans);
                intent.putExtra("TSTAMP", tstamp);
                intent.putExtra("CT", ans_ct);
                intent.putExtra("ID", id);

                startActivityForResult(intent, 5);
            }
            else
            {
                if(new_ans) {
                    set_tstamp();
                }
                vwNo++;
                subQtxt.setText(sub_ques[vwNo]);
                reset_colors();
                if(ans[vwNo] != 0) {
                    rows[vwNo].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                }

            }
            }
        });

        // On the event of the user clicking 'back'
        prevBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwNo--;
                subQtxt.setText(sub_ques[vwNo]);
                reset_colors();
            }
        });

        // On the event of the user clicking an answer in the table view
        rows[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                if(ans[vwNo] != 1) new_ans = true;
                ans[vwNo] = 1;
            }
        });

        rows[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                if(ans[vwNo] != 2) new_ans = true;
                ans[vwNo] = 2;
            }
        });

        rows[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                if(ans[vwNo] != 3) new_ans = true;
                ans[vwNo] = 3;
            }
        });

        rows[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                if(ans[vwNo] != 4) new_ans = true;
                ans[vwNo] = 4;
            }
        });

        rows[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                if(ans[vwNo] != 5) new_ans = true;
                ans[vwNo] = 5;
            }
        });
    }

    public void reset_colors() {
        rows[0].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[2].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[3].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[4].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
    }

    public void update_count() {
        int cnt = 0;
        for(int i = 0; i < size; i++) {
            if(ans[i] != 0) {
                cnt++;
            }
        }
        ans_ct = cnt;
    }

    public void set_tstamp() {
        if(ans[vwNo] != 0) {
            tstamp[vwNo] = System.currentTimeMillis() / 1000L;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
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

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && data != null) {
            // Get answer and timestamp array previously passed to Finish activity
            ans = data.getIntArrayExtra("ANS");
            tstamp = data.getLongArrayExtra("TSTAMP");

            // Reset color
            reset_colors();

            // Reset selection of table view
            if(ans[size - 1] != 0) {
                rows[ans[size - 1]].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            }

            // Reset question and view number
            vwNo = size - 1;
            subQtxt.setText(sub_ques[vwNo]);

        }
    }
}
