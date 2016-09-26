package com.example.jeffmcnd.myapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.example.jeffmcnd.myapp.model.Comment;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class UpdateCommentService extends IntentService {

    private int bevId;
    private int accountId;
    public static final String COMMENT_KEY = "comment";
    public static final String BEVERAGE_ID_KEY = "comment";
    public static final String ACCOUNT_ID_KEY = "comment";

    public UpdateCommentService() {
        super("UpdateCommentService");
    }

    public UpdateCommentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(COMMENT_KEY) &&
                    extras.containsKey(BEVERAGE_ID_KEY) &&
                    extras.containsKey(ACCOUNT_ID_KEY)) {
                String content = extras.getString(COMMENT_KEY);
                bevId = extras.getInt(BEVERAGE_ID_KEY);
                accountId = extras.getInt(ACCOUNT_ID_KEY);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build();

                GcbfService client = retrofit.create(GcbfService.class);
                Call<Comment> postCommentCall = client.postComment(accountId, bevId, content);

                postCommentCall.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()) {
                            Comment comment = response.body();
                            if (comment.error == null) {
                                deleteCommentFromRealm();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {

                    }
                });
            }
        }
    }

    public Realm getRealmInstance() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        return Realm.getDefaultInstance();
    }

    public void deleteCommentFromRealm() {
        Realm realm = getRealmInstance();
        final RealmResults<Comment> results = realm.where(Comment.class)
                                                   .equalTo("beverage_id", bevId)
                                                   .equalTo("user_id", accountId)
                                                   .findAll();
        if (results.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });
        }
        realm.close();
    }
}
