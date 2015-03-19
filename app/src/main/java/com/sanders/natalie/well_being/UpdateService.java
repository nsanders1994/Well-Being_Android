package com.sanders.natalie.well_being;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Natalie on 2/15/2015.
 */
public class UpdateService extends IntentService {

    SurveyDatabaseHandler dbHandler;

    public UpdateService() {
        super("Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHandler = new SurveyDatabaseHandler(getApplicationContext());
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z6S6iux9qyLGcCsAE3vuRvhHWDwFelxzT2nSqKWc", "boXMTOaotk2HgGpxFLdNNPFw1d7WwB7c3G4nPHak");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Calendar current_date = Calendar.getInstance();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Updates");
        query.orderByDescending("updatedAt");

        query.findInBackground(new FindCallback<ParseObject>() {
               public void done(List<ParseObject> updates, ParseException e) {
            if (e == null) {

            } else {
                Calendar update_time = Calendar.getInstance();

                for(int i = 0; i < updates.size(); i++) {
                    ParseObject curr_update = updates.get(i);
                    update_time.setTime(curr_update.getCreatedAt());
                    long time_diff = current_date.getTimeInMillis() - update_time.getTimeInMillis();

                    if(time_diff > 24*60*60*1000) {
                        break;
                    }
                    else {
                        ParseObject updated_survey = curr_update.getParseObject("Survey_Update");
                        String survey_parse_id = updated_survey.getObjectId();

                        if(dbHandler.isNewSurvey(survey_parse_id)) {
                            dbHandler.createSurvey(
                                    survey_parse_id,
                                    updated_survey.getString("Name"),
                                    updated_survey.getString("BaseQ"),
                                    updated_survey.getString("SubQ"),
                                    updated_survey.getInt("Hr"),
                                    updated_survey.getInt("Min"),
                                    updated_survey.getInt("SetType")
                            );
                        }
                        else {
                            dbHandler.setHr(updated_survey.getInt("Hr"), survey_parse_id);
                            dbHandler.setMin(updated_survey.getInt("Min"), survey_parse_id);
                            dbHandler.setBaseQues(updated_survey.getString("BaseQ"), survey_parse_id);
                            dbHandler.setSubQues(updated_survey.getString("SubQ"), survey_parse_id);
                            dbHandler.setName(updated_survey.getString("Name"), survey_parse_id);
                        }

                        set_DialogAlarm(
                               updated_survey.getInt("Hr"),
                               updated_survey.getInt("Min"),
                               dbHandler.parseToTableID(survey_parse_id)
                        );
                    }
                }
            }
            }
        });
    }

    public void set_DialogAlarm(int hr, int min, int table_id) {

        Intent intent = new Intent(UpdateService.this, PopupService.class);
        intent.putExtra("ID", table_id);

        // Alarm is reset at new time
        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                table_id,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set new time
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

        // Set alarm for survey pop-up
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                alarmManager.INTERVAL_DAY,
                pendingIntent);
    }
}
