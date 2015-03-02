package com.sanders.natalie.well_being;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natalie on 2/3/2015.
 */
public class AlertDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION   = 2;
    private static final String DATABASE_NAME   = "wellbeingManager";
    private static final String TABLE_WELLBEING = "wellbeing";
    private static final String KEY_ID          = "id";
    private static final String KEY_DELAYS      = "delays";
    private static final String KEY_POPUP_HR    = "popup_hr";
    private static final String KEY_POPUP_MIN   = "popup_min";
    private static final String KEY_TITLE       = "survey_title";
    private static final String KEY_QUES_LIST   = "ques_list";
    private static final String KEY_BASE_QUES   = "base_ques";
    private static final String KEY_SET_TYPE    = "set_type";


    public AlertDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WELLBEING + "("
                + KEY_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DELAYS    + " INTEGER,"
                + KEY_POPUP_HR  + " INTEGER,"
                + KEY_POPUP_MIN + " INTEGER,"
                + KEY_TITLE     + " STRING,"
                + KEY_BASE_QUES + " STRING,"
                + KEY_QUES_LIST + " STRING,"
                + KEY_SET_TYPE  + " INTEGER)");
    }

    public void init() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID,        0);
        values.put(KEY_DELAYS,    0);
        values.put(KEY_POPUP_HR,  8);
        values.put(KEY_POPUP_MIN, 0);
        values.put(KEY_TITLE,     "");
        values.put(KEY_BASE_QUES, "");
        values.put(KEY_QUES_LIST, "");
        values.put(KEY_SET_TYPE,  0);

        db.insert(TABLE_WELLBEING,null,values);
    }

    public void createSurvey(String title, String base, String [] ques, int hr, int min, int set_type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELAYS,    0);
        values.put(KEY_POPUP_HR,  hr);
        values.put(KEY_POPUP_MIN, min);
        values.put(KEY_TITLE,     title);
        values.put(KEY_BASE_QUES, base);
        values.put(KEY_QUES_LIST, TextUtils.join("|", ques));
        values.put(KEY_SET_TYPE,  set_type);

        db.insert(TABLE_WELLBEING,null,values);
        db.close();
    }

    public int getSurveyCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WELLBEING);

        onCreate(db);
    }

    public List<Integer> getSurveyIDs() {
        List<Integer> ids = new ArrayList<Integer>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);

        if (cursor.moveToFirst()) {
            do {
                ids.add(Integer.parseInt(cursor.getString(0)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return ids;
    }

    public int getDelays(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        // cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);
        cursor.moveToFirst();
        int delays = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DELAYS)));

        cursor.close();
        db.close();

        return delays;
    }

    public int getHr(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);
        cursor.moveToFirst();

        int hr = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_POPUP_HR)));

        cursor.close();
        db.close();

        return hr;
    }

    public int getMin(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);
        cursor.moveToFirst();

        int min = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_POPUP_MIN)));

        cursor.close();
        db.close();

        return min;
    }

    public String getName(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(KEY_TITLE));

        cursor.close();
        db.close();

        return name;
    }

    public String getBaseQues(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);

        cursor.moveToFirst();
        String base = cursor.getString(cursor.getColumnIndex(KEY_BASE_QUES));

        cursor.close();
        db.close();

        return base;
    }

    public String [] getSubQuesArray(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);

        cursor.moveToFirst();
        String ques_list_str = cursor.getString(cursor.getColumnIndex(KEY_QUES_LIST));
        String [] ques_list = ques_list_str.split("|");

        cursor.close();
        db.close();

        return ques_list;
    }

    public int getSetType(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_ID + "==" + id, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);

        cursor.moveToFirst();
        int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SET_TYPE)));

        cursor.close();
        db.close();

        return type;
    }

    public int setDelays(int delays, int id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DELAYS, delays);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "=" + id, null);
        db.close();

        return update;
    }

    public int setHr(int hr) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_POPUP_HR, hr);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "= ?", null);
        db.close();

        return update;
    }

    public int setMin(int min) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_POPUP_MIN, min);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "= ?", null);
        db.close();

        return update;
    }

    public int updateName(String old_name, String new_name) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_BASE_QUES, new_name);

        int update = db.update(TABLE_WELLBEING, values, KEY_BASE_QUES + "=" + old_name, null);
        db.close();

        return update;
    }

    public int updateBaseQues(String base, String name) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASE_QUES, base);

        int update = db.update(TABLE_WELLBEING, values, KEY_BASE_QUES + "=" + name, null);
        db.close();

        return update;
    }

    public int updateQuesList(String name, String [] ques_list) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUES_LIST, TextUtils.join("|", ques_list));

        int update = db.update(TABLE_WELLBEING, values, KEY_BASE_QUES + "=" + name, null);
        db.close();

        return update;
    }
}
