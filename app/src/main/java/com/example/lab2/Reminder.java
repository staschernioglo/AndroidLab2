package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;


public class Reminder implements Parcelable {
    public String name;
    public long time;

    public Reminder(String name, long time) {
        this.name = name;
        this.time = time;
    }

    protected Reminder(Parcel in) {
        name = in.readString();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
