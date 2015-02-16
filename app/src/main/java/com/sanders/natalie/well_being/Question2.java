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


public class Question2 extends Activity {

    public Survey survey = new Survey();
    public RadioGroup ansRdioGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        final Button nextBttn = (Button) findViewById(R.id.bttnNext);
        final Button prevBttn = (Button) findViewById(R.id.bttnBack);
        ansRdioGrp = (RadioGroup) findViewById(R.id.rdioGrpScale);

        // If the Q2 activity is accessed from the Q1 activity, the survey object is retrieved from the Q1 activity
        Intent curr_intent = getIntent();
        Survey s = curr_intent.getParcelableExtra("SURVEY");
        if(s != null) {
            survey = s;
            ansRdioGrp.check(survey.get_q2_id());
        }

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question2.this, Question3.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                if (intent != null) {
                    startActivityForResult(intent, 3);
                }
            }
        });

        prevBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Question2.this, Question1.class);
                update_survey();
                intent.putExtra("SURVEY", survey);

                if(intent != null) {
                    setResult(2, intent);
                    finish();
                }
            }
        });
    }

    public void update_survey() {
        int selectedId = ansRdioGrp.getCheckedRadioButtonId();
        RadioButton ansRdioBttn = (RadioButton) findViewById(selectedId);

        if(selectedId != survey.get_q2_id()) {

            survey.set_q2_id(selectedId);

            if(ansRdioBttn != null) {

                if (!survey.is_q2_answered()) {
                    survey.inc_num_answered();
                }

                survey.set_q2_answered(true);
                survey.set_q2_tstamp();

                if (ansRdioBttn.getText().equals("1 - not at all")) {
                    survey.set_q2(1);
                } else if (ansRdioBttn.getText().equals("2 - Very little")) {
                    survey.set_q2(2);
                } else if (ansRdioBttn.getText().equals("3 - Somewhat")) {
                    survey.set_q2(3);
                } else if (ansRdioBttn.getText().equals("4 - Quite a bit")) {
                    survey.set_q2(4);
                } else if (ansRdioBttn.getText().equals("5 - Extremely")) {
                    survey.set_q2(5);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question2, menu);
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

        if (requestCode == 3 && data != null) {
            Survey s = data.getParcelableExtra("SURVEY");
            if (s != null) {
                survey = s;
            }

            ansRdioGrp.check(survey.get_q2_id());
        }
    }
}
