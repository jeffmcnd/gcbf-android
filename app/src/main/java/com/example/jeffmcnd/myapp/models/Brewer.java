package com.example.jeffmcnd.myapp.models;

import java.util.List;

public class Brewer {
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
}
