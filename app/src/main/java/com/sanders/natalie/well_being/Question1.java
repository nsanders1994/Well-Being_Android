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
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import com.parse.Parse;

import java.util.HashMap;
import java.util.Map;

public class Question1 extends Activity {

    public Survey survey = new Survey();
    public RadioGroup ansRdioGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Z6S6iux9qyLGcCsAE3vuRvhHWDwFelxzT2nSqKWc", "boXMTOaotk2HgGpxFLdNNPFw1d7WwB7c3G4nPHak");

        // Initialize Buttons / Radio Group

        final Button nextBttn = (Button) findViewById(R.id.bttnNext);
        ansRdioGrp            = (RadioGroup) findViewById(R.id.rdioGrpScale);

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question1.this, Question2.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                if (intent != null) {
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    public void update_survey() {
        int selectedId = ansRdioGrp.getCheckedRadioButtonId();
        RadioButton ansRdioBttn = (RadioButton) findViewById(selectedId);
        Toast.makeText(getApplicationContext(), selectedId + " " + survey.get_q1_id(), Toast.LENGTH_SHORT).show();

        if(selectedId != survey.get_q1_id()) {
            Toast.makeText(getApplicationContext(), "update survey", Toast.LENGTH_SHORT).show();

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
