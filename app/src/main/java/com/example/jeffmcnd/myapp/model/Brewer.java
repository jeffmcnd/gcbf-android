package com.example.jeffmcnd.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Brewer implements Parcelable {
    public final int id;
    public final String name;
    public final String blurb;
    public final String imageurl;
    public final String location;
    public final String lat;
    public final String lng;
    public final List<Beverage> beverages;
    public final List<Booth> booths;

    public Brewer(int id, String name, String blurb, String imageurl, String location, String lat, String lng, List<Beverage> beverages, List<Booth> booths) {
        this.id = id;
        this.name = name;
        this.blurb = blurb;
        this.imageurl = imageurl;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.beverages = beverages;
        this.booths = booths;
    }

    @Override
    public String toString() {
        return name;
    }

    protected Brewer(Parcel in) {
        id = in.readInt();
        name = in.readString();
        blurb = in.readString();
        imageurl = in.readString();
        location = in.readString();
        lat = in.readString();
        lng = in.readString();
        if (in.readByte() == 0x01) {
            beverages = new ArrayList<Beverage>();
            in.readList(beverages, Beverage.class.getClassLoader());
        } else {
            beverages = null;
        }
        if (in.readByte() == 0x01) {
            booths = new ArrayList<Booth>();
            in.readList(booths, Booth.class.getClassLoader());
        } else {
            booths = null;
        }
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
        dest.writeString(location);
        dest.writeString(lat);
        dest.writeString(lng);
        if (beverages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(beverages);
        }
        if (booths == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(booths);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Brewer> CREATOR = new Parcelable.Creator<Brewer>() {
        @Override
        public Brewer createFromParcel(Parcel in) {
            return new Brewer(in);
        }

        @Override
        public Brewer[] newArray(int size) {
            return new Brewer[size];
        }
    };
}
