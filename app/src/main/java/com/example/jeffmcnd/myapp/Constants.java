package com.example.jeffmcnd.myapp;

import android.content.Context;

import com.example.jeffmcnd.myapp.model.Comment;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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

    public static int getNextCommentId(Context context) {
        Realm realm = getRealmInstance(context);
        RealmResults<Comment> results = realm.where(Comment.class).findAll();
        if (results.size() == 0) {
            return 0;
        }
        return realm.where(Comment.class).max("id").intValue() + 1;
    }
}
