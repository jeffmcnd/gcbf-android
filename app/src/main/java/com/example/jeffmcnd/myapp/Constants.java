package com.example.jeffmcnd.myapp;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Constants {
    public static final String ACCOUNT_ID_KEY = "account_id";
    public static final String BEVERAGE_ID_KEY = "beverage_id";
    public static final String COMMENT_CONTENT_KEY = "content";
    public static final String SHOULD_POST_FAV = "post_fav";

    public static Realm getRealmInstance(Context context) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        return Realm.getDefaultInstance();
    }
}
