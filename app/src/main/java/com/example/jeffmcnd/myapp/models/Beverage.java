package com.example.jeffmcnd.myapp.models;

public class Beverage {
    public final int id;
    public final String name;
    public final String blurb;
    public final String imageurl;
    public final int brewer_id;

    public Beverage(int id, String name, String blurb, String imageurl, int brewer_id) {
        this.id = id;
        this.name = name;
        this.blurb = blurb;
        this.imageurl = imageurl;
        this.brewer_id = brewer_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
