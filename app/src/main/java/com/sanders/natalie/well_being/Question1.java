package com.sanders.natalie.well_being;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Calendar;

public class Question1 extends Activity {

    public Survey survey = new Survey();
    //public RadioGroup ansRdioGrp;
    TableRow [] rows = new TableRow[5];
    public int ans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_question1);
        setContentView(R.layout.activity_question_1);


        // Initialize Buttons / Radio Group

        final Button nextBttn = (Button) findViewById(R.id.bttnNext);
        //ansRdioGrp            = (RadioGroup) findViewById(R.id.rdioGrpScale);
        rows[0] = (TableRow) findViewById(R.id.tableRow1);
        rows[1] = (TableRow) findViewById(R.id.tableRow2);
        rows[2] = (TableRow) findViewById(R.id.tableRow3);
        rows[3] = (TableRow) findViewById(R.id.tableRow4);
        rows[4] = (TableRow) findViewById(R.id.tableRow5);

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question1.this, Question2.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                startActivityForResult(intent, 2);
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
        /*int selectedId = ansRdioGrp.getCheckedRadioButtonId();
        RadioButton ansRdioBttn = (RadioButton) findViewById(selectedId);

        if(selectedId != survey.get_q1_id()) {

            survey.set_q1_id(selectedId);

            if(ansRdioBttn != null) {

                if (!survey.is_q1_answered()) {
                    survey.inc_num_answered();
                }

                survey.set_q1_answered(true);
                survey.set_q1_tstamp();

                if (ansRdioBttn.getText().equals("1 - not at all")) {
                    survey.set_q1(1);
                } else if (ansRdioBttn.getText().equals("2 - Very little")) {
                    survey.set_q1(2);
                } else if (ansRdioBttn.getText().equals("3 - Somewhat")) {
                    survey.set_q1(3);
                } else if (ansRdioBttn.getText().equals("4 - Quite a bit")) {
                    survey.set_q1(4);
                } else if (ansRdioBttn.getText().equals("5 - Extremely")) {
                    survey.set_q1(5);
                }
            }
        }*/

        if(ans != 0) {
            if (!survey.is_q1_answered()) {
                survey.inc_num_answered();
            }

            survey.set_q1_answered(true);
            survey.set_q1_tstamp();
            survey.set_q1(ans);
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

        if (requestCode == 2 && data != null) {
            Survey s = data.getParcelableExtra("SURVEY");
            if (s != null) {
                survey = s;
            }

            reset_colors();
            ans = survey.get_q1();

            if(ans != 0) {
                rows[ans - 1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            }
            //ansRdioGrp.check(survey.get_q1_id());
        }
    }
}
