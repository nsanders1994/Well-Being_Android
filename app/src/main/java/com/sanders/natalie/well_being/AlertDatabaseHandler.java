package com.sanders.natalie.well_being;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Natalie on 2/3/2015.
 */
public class AlertDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION   = 4;
    private static final String DATABASE_NAME   = "WellbeingManager";
    private static final String TABLE_WELLBEING = "Wellbeing";
    private static final String KEY_ID          = "ID";
    private static final String KEY_PARSE_ID    = "ParseID";
    private static final String KEY_DELAYS      = "Delays";
    private static final String KEY_POPUP_HR    = "PopupHr";
    private static final String KEY_POPUP_MIN   = "PopupMin";
    private static final String KEY_TITLE       = "Name";
    private static final String KEY_QUES_LIST   = "SubQ";
    private static final String KEY_BASE_QUES   = "BaseQ";
    private static final String KEY_SET_TYPE    = "SetType";


    public AlertDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WELLBEING + "("
                + KEY_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PARSE_ID  + " STRING,"
                + KEY_DELAYS    + " INTEGER,"
                + KEY_POPUP_HR  + " INTEGER,"
                + KEY_POPUP_MIN + " INTEGER,"
                + KEY_TITLE     + " STRING,"
                + KEY_BASE_QUES + " STRING,"
                + KEY_QUES_LIST + " STRING,"
                + KEY_SET_TYPE  + " INTEGER)");
    }

    public Boolean isNewSurvey(String parse_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_WELLBEING, new String[] {KEY_ID},
                KEY_PARSE_ID + "==" + parse_id, null, null, null, null);

        return (cursor == null);
    }

    public void createSurvey(String parse_id, String title, String base, String ques, int hr, int min, int set_type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PARSE_ID, parse_id);
        values.put(KEY_DELAYS,    0);
        values.put(KEY_POPUP_HR,  hr);
        values.put(KEY_POPUP_MIN, min);
        values.put(KEY_TITLE,     title);
        values.put(KEY_BASE_QUES, base);
        values.put(KEY_QUES_LIST, ques); //TextUtils.join("|", ques));
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
        Cursor cursor = db.rawQuery("SELECT rowid FROM " + TABLE_WELLBEING, null);

        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return ids;
    }

    public int getDelays(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_DELAYS +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        int delays = cursor.getInt(0); //Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DELAYS)));

        cursor.close();
        db.close();

        return delays;
    }

    public int getHr(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_POPUP_HR +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        int hr = cursor.getInt(0); //Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_POPUP_HR)));

        cursor.close();
        db.close();

        return hr;
    }

    public int getMin(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_POPUP_MIN +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        int min = cursor.getInt(0); //Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_POPUP_MIN)));

        cursor.close();
        db.close();

        return min;
    }

    public String getName(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_TITLE +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        String name = cursor.getString(0);

        cursor.close();
        db.close();

        return name;
    }

    public String getBaseQues(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_BASE_QUES +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        String base = cursor.getString(0); //cursor.getString(cursor.getColumnIndex(KEY_BASE_QUES));

        cursor.close();
        db.close();

        return base;
    }

    public List<String> getSubQuesArray(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_QUES_LIST +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        String ques_list_str = cursor.getString(0); //cursor.getString(cursor.getColumnIndex(KEY_QUES_LIST));
        List<String> ques_list = Arrays.asList(ques_list_str.split("\\s*\\|\\s*"));
        //String [] ques_list = ques_list_str.split("|");

        cursor.close();
        db.close();

        return ques_list;
    }

    public int getSetType(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_SET_TYPE +
                " FROM " + TABLE_WELLBEING +
                " WHERE rowid = " + id,
                null
        );

        cursor.moveToFirst();
        int type = cursor.getInt(0); //Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SET_TYPE)));

        cursor.close();
        db.close();

        return type;
    }

    public int getTableID(String parse_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT rowid FROM " + TABLE_WELLBEING +
                " WHERE " + KEY_PARSE_ID + " = \"" + parse_id + "\"",
                null
        );
        cursor.moveToFirst();
        int table_id = cursor.getInt(0); //cursor.getInt(cursor.getColumnIndex(KEY_ID));

        cursor.close();
        db.close();

        return table_id;
    }

    public int setDelays(int delays, int id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DELAYS, delays);

        int update = db.update(TABLE_WELLBEING, values, "rowid=" + id, null);
        db.close();

        return update;
    }

    public int setHr(int hr, String parse_id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_POPUP_HR, hr);

        int update = db.update(TABLE_WELLBEING, values, KEY_PARSE_ID + "=" + parse_id, null);
        db.close();

        return update;
    }

    public int setMin(int min, String parse_id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_POPUP_MIN, min);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "=" + parse_id, null);
        db.close();

        return update;
    }

    public int setName(String new_name, String parse_id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_BASE_QUES, new_name);

        int update = db.update(TABLE_WELLBEING, values, KEY_PARSE_ID + "=" + parse_id, null);
        db.close();

        return update;
    }

    public int setBaseQues(String base, String parse_id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASE_QUES, base);

        int update = db.update(TABLE_WELLBEING, values, KEY_PARSE_ID + "=" + parse_id, null);
        db.close();

        return update;
    }

    public int setSubQues(String ques_list, String parse_id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUES_LIST, ques_list); //TextUtils.join("|", ques_list));

        int update = db.update(TABLE_WELLBEING, values, KEY_PARSE_ID + "=" + parse_id, null);
        db.close();

        return update;
    }
}
