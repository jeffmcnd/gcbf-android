package com.example.jeffmcnd.myapp.models;

public class Booth {
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
}
