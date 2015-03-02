package com.sanders.natalie.well_being;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class Question4 extends Activity {

    public Survey survey = new Survey();
    TableRow [] rows = new TableRow[5];
    public int ans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question4);

        final Button nextBttn = (Button) findViewById(R.id.bttnNext);
        final Button prevBttn = (Button) findViewById(R.id.bttnBack);
        rows[0] = (TableRow) findViewById(R.id.tableRow1);
        rows[1] = (TableRow) findViewById(R.id.tableRow2);
        rows[2] = (TableRow) findViewById(R.id.tableRow3);
        rows[3] = (TableRow) findViewById(R.id.tableRow4);
        rows[4] = (TableRow) findViewById(R.id.tableRow5);

        // If the Q2 activity is accessed from the Q1 activity, the survey object is retrieved from the Q1 activity
        Intent curr_intent = getIntent();
        Survey s = curr_intent.getParcelableExtra("SURVEY");
        if(s != null) {
            survey = s;
            ans = survey.get_q4();

            reset_colors();
            if(ans != 0) {
                rows[ans - 1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            }

        }

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question4.this, Finish.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                if (intent != null) {
                    startActivityForResult(intent, 5);
                }
            }
        });

        prevBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question4.this, Question3.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                if(intent != null) {
                    setResult(4, intent);
                    finish();
                }
            }
        });

        rows[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                ans = 1;
            }
        });

        rows[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                ans = 2;
            }
        });

        rows[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                ans = 3;
            }
        });

        rows[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                ans = 4;
            }
        });

        rows[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_colors();
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                ans = 5;
            }
        });
    }

    public void update_survey() {
        if(ans != 0) {
            if (!survey.is_q4_answered()) {
                survey.inc_num_answered();
            }

            survey.set_q4_answered(true);
            survey.set_q4_tstamp();
            survey.set_q4(ans);
        }
    }

    public void reset_colors() {
        rows[0].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[2].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[3].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        rows[4].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question4, menu);
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
            Survey s = data.getParcelableExtra("SURVEY");
            if (s != null) {
                survey = s;
            }

            reset_colors();
            ans = survey.get_q4();

            if(ans != 0) {
                rows[ans - 1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        }
    }
}
