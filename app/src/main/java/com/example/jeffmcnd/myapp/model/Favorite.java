package com.example.jeffmcnd.myapp.model;

import io.realm.RealmObject;

public class Favorite extends RealmObject {
    public int id;
    public int accountId;
    public int beverageId;

    public Favorite() {

    }

    public Favorite(int id, int accountId, int beverageId) {
        this.id = id;
        this.accountId = accountId;
        this.beverageId = beverageId;
    }
}
