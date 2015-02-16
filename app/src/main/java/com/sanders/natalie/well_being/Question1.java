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
import com.parse.Parse;

import java.util.Calendar;

public class Question1 extends Activity {

    public Survey survey = new Survey();
    public RadioGroup ansRdioGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);
        //setContentView(R.layout.activity_question_1);


        // Initialize Buttons / Radio Group

        final Button nextBttn = (Button) findViewById(R.id.bttnNext);
        ansRdioGrp            = (RadioGroup) findViewById(R.id.rdioGrpScale);
        /*TableRow R1 = (TableRow) findViewById(R.id.tableRow1);
        TableRow R2 = (TableRow) findViewById(R.id.tableRow2);
        TableRow R3 = (TableRow) findViewById(R.id.tableRow3);
        TableRow R4 = (TableRow) findViewById(R.id.tableRow4);
        TableRow R5 = (TableRow) findViewById(R.id.tableRow5);*/

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question1.this, Question2.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                startActivityForResult(intent, 2);
            }
        });

        /*R1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(getResources().getColor(android.res.));
            }
        });

        R2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        R3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        R4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        R5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }



    public void update_survey() {
        int selectedId = ansRdioGrp.getCheckedRadioButtonId();
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

        if (requestCode == 2 && data != null) {
            Survey s = data.getParcelableExtra("SURVEY");
            if (s != null) {
                survey = s;
            }

            ansRdioGrp.check(survey.get_q1_id());
        }
    }
}
