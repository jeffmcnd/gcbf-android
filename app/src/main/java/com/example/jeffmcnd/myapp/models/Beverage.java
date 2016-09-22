package com.example.jeffmcnd.myapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Beverage implements Parcelable {
    public final int id;
    public final String name;
    public final String blurb;
    public final String imageurl;
    public final int brewer_id;
    public final int count;

    public Beverage(int id, String name, String blurb, String imageurl, int brewer_id, int count) {
        this.id = id;
        this.name = name;
        this.blurb = blurb;
        this.imageurl = imageurl;
        this.brewer_id = brewer_id;
        this.count = count;
    }

    @Override
    public String toString() {
        return name;
    }

    protected Beverage(Parcel in) {
        id = in.readInt();
        name = in.readString();
        blurb = in.readString();
        imageurl = in.readString();
        brewer_id = in.readInt();
        count = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(blurb);
        dest.writeString(imageurl);
        dest.writeInt(brewer_id);
        dest.writeInt(count);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Beverage> CREATOR = new Parcelable.Creator<Beverage>() {
        @Override
        public Beverage createFromParcel(Parcel in) {
            return new Beverage(in);
        }

        @Override
        public Beverage[] newArray(int size) {
            return new Beverage[size];
        }
    };
}
