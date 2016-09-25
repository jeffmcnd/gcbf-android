package com.example.jeffmcnd.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    public final int id;
    public final String content;
    public final int user_id;
    public final int beverage_id;

    public Comment(int id, String content, int user_id, int beverage_id) {
        this.id = id;
        this.content = content;
        this.user_id = user_id;
        this.beverage_id = beverage_id;
    }

    @Override
    public String toString() {
        return content;
    }

    protected Comment(Parcel in) {
        id = in.readInt();
        content = in.readString();
        user_id = in.readInt();
        beverage_id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeInt(user_id);
        dest.writeInt(beverage_id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
