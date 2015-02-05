package com.sanders.natalie.well_being;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natalie on 2/3/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION   = 1;
    private static final String DATABASE_NAME   = "wellbeingManager";
    private static final String KEY_ID          = "id";
    private static final String TABLE_WELLBEING = "wellbeing";
    private static final String KEY_DELAYS      = "delays";
    private static final String KEY_POPUP_HR    = "popup_hr";
    private static final String KEY_POPUP_MIN   = "popup_min";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WELLBEING + "("
                + KEY_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DELAYS    + " INTEGER,"
                + KEY_POPUP_HR  + " INTEGER,"
                + KEY_POPUP_MIN + " INTEGER)");
    }

    public void init() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, 0);
        values.put(KEY_DELAYS, 0);
        values.put(KEY_POPUP_HR, 8);
        values.put(KEY_POPUP_MIN, 0);

        db.insert(TABLE_WELLBEING,null,values);
    }

    public int isInitialized(){
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

    public int getDelays(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);
        cursor.moveToFirst();

        int delays = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DELAYS)));

        cursor.close();
        db.close();

        return delays;
    }

    public int getHr(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);
        cursor.moveToFirst();

        int hr = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_POPUP_HR)));

        cursor.close();
        db.close();

        return hr;
    }

    public int getMin(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WELLBEING, null);
        cursor.moveToFirst();

        int min = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_POPUP_MIN)));

        cursor.close();
        db.close();

        return min;
    }

    public int incDelays() {
        int delays = getDelays();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DELAYS, delays++);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setDelays(int delays) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DELAYS, delays);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setHr(int hr) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_POPUP_HR, hr);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }

    public int setMin(int min) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_POPUP_MIN, min);

        int update = db.update(TABLE_WELLBEING, values, KEY_ID + "=" + 0, null);
        db.close();

        return update;
    }



}
