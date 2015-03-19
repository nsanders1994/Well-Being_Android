package com.sanders.natalie.well_being;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DelayExplanation extends Activity {
    int ID;
    int setType;
    EditText expTxt;
    SurveyDatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get ID of survey
        Intent caller = getIntent();
        ID = caller.getIntExtra("ID", 1);
        setContentView(R.layout.activity_delay_explanation);

        // Initialize database and get survey values
        dbHandler = new SurveyDatabaseHandler(getApplicationContext());
        setType = dbHandler.getSetType(ID);

        // Initialize View
        final Button submitBttn = (Button) findViewById(R.id.bttnNext);
        expTxt = (EditText) findViewById(R.id.editExplanation);

        // Wait for user to fill in response
        expTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                boolean filled = String.valueOf(expTxt.getText()).trim().length() != 0;
                if(filled) {
                    submitBttn.setEnabled(true);
                }
                else if (!filled) {
                    submitBttn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // When the user clicks 'Submit'
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                dbHandler.setExplanation(expTxt.getText().toString(), ID);

                if(setType == 1) {
                    intent = new Intent(DelayExplanation.this, Questions_MultChoice.class);
                }
                else if(setType == 2) {
                    intent = new Intent(DelayExplanation.this, Questions_Slider.class);
                }
                else {
                    intent = new Intent(DelayExplanation.this, Questions_MultChoice.class);
                }

                intent.putExtra("ID", ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delay_explanation, menu);
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
