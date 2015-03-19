package com.sanders.natalie.well_being;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;




public class Start extends Activity {
    SurveyDatabaseHandler dbHandler;
    List<Integer> survey_ids = new ArrayList<Integer>();
    ListView startListView;
    StartListAdapter startListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new SurveyDatabaseHandler(getApplicationContext());

        // If app was just installed get all surveys from Parse and store in database
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.prev_started), false);
        if(!previouslyStarted) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Surveys");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> surveys, ParseException e) {
                    if (e == null) {
                        Log.d("Updates", "Importing surveys");
                        for(int i = 0; i < surveys.size(); i++) {
                            ParseObject curr_survey = surveys.get(i);
                            int hour = curr_survey.getInt("Hr");
                            int minute = curr_survey.getInt("Min");

                            dbHandler.createSurvey(
                                    curr_survey.getObjectId(),
                                    curr_survey.getString("Name"),
                                    curr_survey.getString("BaseQ"),
                                    curr_survey.getString("SubQ"),
                                    hour,
                                    minute,
                                    curr_survey.getInt("SetType")
                            );

                            start_DialogAlarm(hour, minute, i);
                        }
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.prev_started), Boolean.TRUE);
                        edit.commit();
                    } else {
                        Log.d("Updates", "Parse initial survey request failed");
                    }
                }
            });

        }


        setContentView(R.layout.list_view);

        startListView    = (ListView) findViewById(R.id.listView);
        startListAdapter = new StartListAdapter();
        startListView.setAdapter(startListAdapter);

        dbHandler = new SurveyDatabaseHandler(getApplicationContext());
        survey_ids = dbHandler.getSurveyIDs();

        // Start background service to check for updated popup times
        start_UpdatesService();

        startListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int curr_id = survey_ids.get(position);
                int set_type = dbHandler.getSetType(curr_id);
                int hr = dbHandler.getHr(curr_id);
                int min = dbHandler.getMin(curr_id);


                Intent i;
                int curr_hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int curr_min = Calendar.getInstance().get(Calendar.MINUTE);

                if ( curr_hr > hr + 1 || curr_hr < hr) {
                    Toast.makeText(getApplicationContext(), "Survey inaccessible at this time", Toast.LENGTH_SHORT);
                }
                else if (set_type == 1) {
                    i = new Intent(Start.this, Questions_MultChoice.class);
                    i.putExtra("ID",survey_ids.get(position));
                    startActivity(i);
                }
                else if (set_type == 2) {
                    i = new Intent(Start.this, Questions_Slider.class);
                    i.putExtra("ID",survey_ids.get(position));
                    startActivity(i);
                }
            }
        });
    }
    public void start_UpdatesService() {

        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                1,
                new Intent(getApplicationContext(), UpdateService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    public void start_DialogAlarm(int hr, int min, int table_id) {

        // Alarm will trigger the pop-up dialog
        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                table_id,
                new Intent(getApplicationContext(), PopupService.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set time for survey pop-up
        Calendar cal = Calendar.getInstance();

        // Get alarm time
        int curr_hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curr_min = Calendar.getInstance().get(Calendar.MINUTE);

        // If it's after the alarm time, schedule for next day
        Toast.makeText(getApplicationContext(), curr_hr + ":" + curr_min + " " + hr + ":" + min, Toast.LENGTH_SHORT).show();
        if ( curr_hr > hr || curr_hr == hr && curr_min > min) {
            cal.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }

        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

        // Set alarm for survey pop-up to go off at default of 8:00 AM
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class StartListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dbHandler.getSurveyCount();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if(arg1==null)
            {
                LayoutInflater inflater = (LayoutInflater) Start.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.list_item, arg2,false);
            }

            TextView name = (TextView)arg1.findViewById(R.id.txtBaseQ);
            TextView time = (TextView)arg1.findViewById(R.id.txtSubQ4);

            String t_str;
            int hr = dbHandler.getHr(arg0 + 1);
            String hr_str;
            int min = dbHandler.getMin(arg0 + 1);
            String min_str = ("00" + min).substring(String.valueOf(min).length());

            // Get hour string
            if      (hr == 0)  hr_str = "12";
            else if (hr > 12)  hr_str = String.valueOf(hr % 12);
            else               hr_str = String.valueOf(hr);

            if (hr >= 12) {
                int end_hr;
                String end_zone = "PM";

                if(hr == 12) {
                    end_hr = 1;
                }
                else {
                    end_hr = Integer.parseInt(hr_str) + 1;
                    if(hr == 11) {
                        end_zone = "AM";
                    }
                }

                t_str = hr_str + ":" + min_str + " PM - " + end_hr + ":" + min_str + " " + end_zone;
            }
            else {
                String end_zone = "AM";

                if (hr == 11) {
                    end_zone = "PM";
                }
                t_str = hr_str + ":" + min_str + " AM - " + String.valueOf(hr + 1) + ":" + min_str + " " + end_zone;
            }

            // Set strings;
            time.setText(t_str);
            name.setText(dbHandler.getName(arg0 + 1));

            // Gray out text if survey is not available
            int curr_hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int curr_min = Calendar.getInstance().get(Calendar.MINUTE);

            if ( curr_hr > hr + 1|| curr_hr < hr) {
                time.setTextColor(getResources().getColor(android.R.color.darker_gray));
                name.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }

            return arg1;
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}


