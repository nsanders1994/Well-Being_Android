package com.sanders.natalie.well_being;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Natalie Sandesr on 1/26/2015.
 */
public class Survey implements Parcelable{
    // numeric values correlating to the answer given
    private int _q1, _q2, _q3, _q4;
    // IDs of the button pressed for each question
    private int _id;
    // number of questions answered in the survey
    private int _num_answered;
    // timestamps of when the questions were answered
    private long _q1_tstamp, _q2_tstamp, _q3_tstamp, _q4_tstamp;
    // indicates which questions have been answered
    private boolean _q1_answered, _q2_answered, _q3_answered, _q4_answered;

    public Survey() {
        // If the user skips a question, the value of the question will be zero
        _q1           = 0;
        _q2           = 0;
        _q3           = 0;
        _q4           = 0;
        _id           = -1;
        _num_answered = 0;

        // to indicate the timestamp is not yet set
        _q1_tstamp    = 0;
        _q2_tstamp    = 0;
        _q3_tstamp    = 0;
        _q4_tstamp    = 0;


        _q1_answered  = false;
        _q2_answered  = false;
        _q3_answered  = false;
        _q4_answered  = false;
    }

    public Survey(int q1, int q2, int q3, int q4, long q1_t, long q2_t, long q3_t, long q4_t, int id) {
        // If the user skips a question, the value of the question will be zero
        _q1           = q1;
        _q2           = q2;
        _q3           = q3;
        _q4           = q4;
        _id           = id;
        _num_answered = 0;

        // to indicate the timestamp is not yet set
        _q1_tstamp    = q1_t;
        _q2_tstamp    = q2_t;
        _q3_tstamp    = q3_t;
        _q4_tstamp    = q4_t;


        _q1_answered  = false;
        _q2_answered  = false;
        _q3_answered  = false;
        _q4_answered  = false;
    }

    // Variable "get" functions

    public int     get_q1()           {return _q1;}
    public int     get_q2()           {return _q2;}
    public int     get_q3()           {return _q3;}
    public int     get_q4()           {return _q4;}
    public int     get_id()           {return _id;}
    public int     get_num_answered() {return _num_answered;}
    public long    get_q1_tstamp()    {return _q1_tstamp;}
    public long    get_q2_tstamp()    {return _q2_tstamp;}
    public long    get_q3_tstamp()    {return _q3_tstamp;}
    public long    get_q4_tstamp()    {return _q4_tstamp;}
    public boolean is_q1_answered()   {return _q1_answered;}
    public boolean is_q2_answered()   {return _q2_answered;}
    public boolean is_q3_answered()   {return _q3_answered;}
    public boolean is_q4_answered()   {return _q4_answered;}

    // Variable "set" functions

    public void inc_num_answered()                {_num_answered++;}
    public void set_q1(int ans)                   {_q1 = ans;}
    public void set_q2(int ans)                   {_q2 = ans;}
    public void set_q3(int ans)                   {_q3 = ans;}
    public void set_q4(int ans)                   {_q4 = ans;}
    public void set_id(int id)                    {_id = id;}
    public void set_q1_answered(boolean answered) {_q1_answered = answered;}
    public void set_q2_answered(boolean answered) {_q2_answered = answered;}
    public void set_q3_answered(boolean answered) {_q3_answered = answered;}
    public void set_q4_answered(boolean answered) {_q4_answered = answered;}
    public void set_q1_tstamp() {
        // Set Unix timestamp to indicate when question 1.1 was answered
        _q1_tstamp = System.currentTimeMillis() / 1000L;
    }
    public void set_q2_tstamp() {
        // Set Unix timestamp to indicate when question 1.2 was answered
        _q2_tstamp = System.currentTimeMillis() / 1000L;
    }
    public void set_q3_tstamp() {
        // Set Unix timestamp to indicate when question 1.1 was answered
        _q3_tstamp = System.currentTimeMillis() / 1000L;
    }
    public void set_q4_tstamp() {
        // Set Unix timestamp to indicate when question 1.2 was answered
        _q4_tstamp = System.currentTimeMillis() / 1000L;
    }


    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_q1);
        out.writeInt(_q2);
        out.writeInt(_q3);
        out.writeInt(_q4);
        out.writeInt(_id);
        out.writeInt(_num_answered);
        out.writeLong(_q1_tstamp);
        out.writeLong(_q2_tstamp);
        out.writeLong(_q3_tstamp);
        out.writeLong(_q4_tstamp);
        out.writeByte((byte) (_q1_answered ? 1:0));
        out.writeByte((byte) (_q2_answered ? 1:0));
        out.writeByte((byte) (_q3_answered ? 1:0));
        out.writeByte((byte) (_q4_answered ? 1:0));

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Survey> CREATOR = new Parcelable.Creator<Survey>() {
        public Survey createFromParcel(Parcel in) {
            return new Survey(in);
        }

        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Survey(Parcel in) {
        _q1           =  in.readInt();
        _q2           =  in.readInt();
        _q3           =  in.readInt();
        _q4           =  in.readInt();
        _id           =  in.readInt();
        _num_answered =  in.readInt();
        _q1_tstamp    =  in.readLong();
        _q2_tstamp    =  in.readLong();
        _q3_tstamp    =  in.readLong();
        _q4_tstamp    =  in.readLong();
        _q1_answered  = (in.readByte() != ((byte) 0));
        _q2_answered  = (in.readByte() != ((byte) 0));
        _q3_answered  = (in.readByte() != ((byte) 0));
        _q4_answered  = (in.readByte() != ((byte) 0));
    }
}
