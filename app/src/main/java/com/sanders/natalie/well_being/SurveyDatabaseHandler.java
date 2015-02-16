package com.sanders.natalie.well_being;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natalie on 2/16/2015.
 */
public class SurveyDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION   = 2;
    private static final String DATABASE_NAME   = "surveysManager";
    private static final String KEY_ID          = "id";
    private static final String TABLE_SURVEY    = "survey";
    private static final String KEY_Q1          = "q1";
    private static final String KEY_Q2          = "q2";
    private static final String KEY_Q3          = "q3";
    private static final String KEY_Q4          = "q4";
    private static final String KEY_Q1_T        = "q1_t";
    private static final String KEY_Q2_T        = "q2_t";
    private static final String KEY_Q3_T        = "q3_t";
    private static final String KEY_Q4_T        = "q4_t";



    public SurveyDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SURVEY + "("
                + KEY_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_Q1   + " INTEGER,"
                + KEY_Q2   + " INTEGER,"
                + KEY_Q3   + " INTEGER,"
                + KEY_Q4   + " INTEGER,"
                + KEY_Q1_T + " LONG,"
                + KEY_Q2_T + " LONG,"
                + KEY_Q3_T + " LONG,"
                + KEY_Q4_T + " LONG)");
    }

    public void init() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, 0);
        values.put(KEY_Q1, 0);
        values.put(KEY_Q2, 0);
        values.put(KEY_Q3, 0);
        values.put(KEY_Q4, 0);
        values.put(KEY_Q1_T, 0);
        values.put(KEY_Q2_T, 0);
        values.put(KEY_Q3_T, 0);
        values.put(KEY_Q4_T, 0);

        db.insert(TABLE_SURVEY,null,values);
    }

    public boolean isEmpty(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURVEY, null);

        boolean empty = false;
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count == 0) {
            empty = true;
        }

        return empty;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY);

        onCreate(db);
    }

    public Survey getSurvey(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURVEY, null);
        cursor.moveToFirst();

        int q1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q1)));
        int q2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q2)));
        int q3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q3)));
        int q4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q4)));

        long q1_t = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_Q1_T)));
        long q2_t = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_Q2_T)));
        long q3_t = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_Q3_T)));
        long q4_t = Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_Q4_T)));

        Survey survey = new Survey(q1, q2, q3, q4, q1_t, q2_t, q3_t, q4_t);

        cursor.close();
        db.close();

        return survey;
    }

    public int putSurvey(Survey survey) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q1, survey.get_q1());
        values.put(KEY_Q2, survey.get_q2());
        values.put(KEY_Q3, survey.get_q3());
        values.put(KEY_Q4, survey.get_q4());

        values.put(KEY_Q1_T, survey.get_q1_tstamp());
        values.put(KEY_Q2_T, survey.get_q2_tstamp());
        values.put(KEY_Q3_T, survey.get_q3_tstamp());
        values.put(KEY_Q4_T, survey.get_q4_tstamp());

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    /*public int getQ1(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURVEY, null);
        cursor.moveToFirst();

        int Q1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q1)));

        cursor.close();
        db.close();

        return Q1;
    }*/

    public int getQ2(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURVEY, null);
        cursor.moveToFirst();

        int Q2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q2)));

        cursor.close();
        db.close();

        return Q2;
    }

    public int getQ3(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURVEY, null);
        cursor.moveToFirst();

        int Q3 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q3)));

        cursor.close();
        db.close();

        return Q3;
    }

    public int getQ4(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURVEY, null);
        cursor.moveToFirst();

        int Q4 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_Q4)));

        cursor.close();
        db.close();

        return Q4;
    }

    public int setQ1(int Q1) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q1, Q1);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ2(int Q2) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q2, Q2);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ3(int Q3) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q3, Q3);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ4(int Q4) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q4, Q4);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ1_T(int Q1T) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q1_T, Q1T);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ2_T(int Q2T) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q2_T, Q2T);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ3_T(int Q3T) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q3_T, Q3T);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setQ4_T(int Q4T) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_Q4_T, Q4T);

        int update = db.update(TABLE_SURVEY, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }


}
