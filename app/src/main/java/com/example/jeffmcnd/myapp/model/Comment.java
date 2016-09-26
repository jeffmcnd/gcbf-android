package com.example.jeffmcnd.myapp.model;

import io.realm.RealmObject;

public class Comment extends RealmObject /*implements Parcelable*/ {
    public int id;
    public String content;
    public int user_id;
    public int beverage_id;
    public String error;

    public Comment() {}

    public Comment(int id, String content, int user_id, int beverage_id, String error) {
        this.id = id;
        this.content = content;
        this.user_id = user_id;
        this.beverage_id = beverage_id;
        this.error = error;
    }

    @Override
    public String toString() {
        return content;
    }

//    protected Comment(Parcel in) {
//        id = in.readInt();
//        content = in.readString();
//        user_id = in.readInt();
//        beverage_id = in.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(content);
//        dest.writeInt(user_id);
//        dest.writeInt(beverage_id);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
//        @Override
//        public Comment createFromParcel(Parcel in) {
//            return new Comment(in);
//        }
//
//        @Override
//        public Comment[] newArray(int size) {
//            return new Comment[size];
//        }
//    };
}
