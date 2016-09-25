package com.example.jeffmcnd.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Booth implements Parcelable {
    public final int id;
    public final int booth;
    public final int brewer_id;

    public Booth(int id, int booth, int brewer_id) {
        this.id = id;
        this.booth = booth;
        this.brewer_id = brewer_id;
    }

    @Override
    public String toString() {
        return String.valueOf(booth);
    }

    protected Booth(Parcel in) {
        id = in.readInt();
        booth = in.readInt();
        brewer_id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(booth);
        dest.writeInt(brewer_id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Booth> CREATOR = new Parcelable.Creator<Booth>() {
        @Override
        public Booth createFromParcel(Parcel in) {
            return new Booth(in);
        }

        @Override
        public Booth[] newArray(int size) {
            return new Booth[size];
        }
    };
}
