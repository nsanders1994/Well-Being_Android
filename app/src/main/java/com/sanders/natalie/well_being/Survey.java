package com.sanders.natalie.well_being;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Natalie on 1/26/2015.
 */
public class Survey implements Parcelable{
    private int _q1, _q2;                       // numeric values correlating to the answer given
    private int _num_answered;                  // number of questions answered in the survey
    private long _q1_tstamp, _q2_tstamp;        // timestamps of when the questions were answered
    private boolean _q1_answered, _q2_answered; // indicates which questions have been answered

    public Survey(int q1, int q2, int num_answered, long tstamp1,
                  long tstamp2, boolean answered1, boolean answered2) {

        _q1           = q1;
        _q2           = q2;
        _num_answered = num_answered;
        _q1_tstamp    = tstamp1;
        _q2_tstamp    = tstamp2;
        _q1_answered  = answered1;
        _q2_answered  = answered2;
    }

    public Survey() {
        // If the user skips a question, the value of the question will be zero
        _q1           = 0;
        _q2           = 0;
        _num_answered = 0;

        _q1_tstamp    = -1; // to indicate the timestamp is not yet set
        _q2_tstamp    = -1; // to indicate the timestamp is not yet set

        _q1_answered  = false;
        _q1_answered  = false;
    }

    // Variable "get" functions

    public int     get_q1()           {return _q1;}
    public int     get_q2()           {return _q2;}
    public int     get_num_answered() {return _num_answered;}
    public long    get_q1_tstamp()    {return _q1_tstamp;}
    public long    get_q2_tstamp()    {return _q2_tstamp;}
    public boolean is_q1_answered()   {return _q1_answered;}
    public boolean is_q2_answered()   {return _q2_answered;}

    // Variable "set" functions

    public void inc_num_answered()                {_num_answered++;}
    public void set_q1(int ans)                   {_q1 = ans;}
    public void set_q2(int ans)                   {_q2 = ans;}
    public void set_q1_answered(boolean answered) {_q1_answered = answered;}
    public void set_q2_answered(boolean answered) {_q2_answered = answered;}
    public void set_q1_tstamp() {
        // Set Unix timestamp to indicate when question 1.1 was answered
        _q1_tstamp = System.currentTimeMillis() / 1000L;
    }
    public void set_q2_tstamp() {
        // Set Unix timestamp to indicate when question 1.2 was answered
        _q2_tstamp = System.currentTimeMillis() / 1000L;
    }


    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_q1);
        out.writeInt(_q2);
        out.writeInt(_num_answered);
        out.writeLong(_q1_tstamp);
        out.writeLong(_q2_tstamp);
        out.writeByte((byte) (_q1_answered ? 1:0));
        out.writeByte((byte) (_q2_answered ? 1:0));

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
        _num_answered =  in.readInt();
        _q1_tstamp    =  in.readLong();
        _q2_tstamp    =  in.readLong();
        _q1_answered  = (in.readByte() != ((byte) 0));
        _q2_answered  = (in.readByte() != ((byte) 0));
    }
}
