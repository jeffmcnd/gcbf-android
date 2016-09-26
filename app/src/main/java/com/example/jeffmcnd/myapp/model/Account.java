package com.example.jeffmcnd.myapp.model;

import io.realm.RealmObject;

public class Account extends RealmObject {

    public int id;
    public String name;
    public String error;

    @Override
    public String toString() {
        return name;
    }

//    protected Account(Parcel in) {
//        id = in.readInt();
//        name = in.readString();
//        error = in.readString();
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
//        dest.writeString(name);
//        dest.writeString(error);
//    }
//
//    final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
//        @Override
//        public Account createFromParcel(Parcel in) {
//            return new Account(in);
//        }
//
//        @Override
//        public Account[] newArray(int size) {
//            return new Account[size];
//        }
//    };
//
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getError() { return error; }
//    public void setError(String error) { this.error = error; }
}
