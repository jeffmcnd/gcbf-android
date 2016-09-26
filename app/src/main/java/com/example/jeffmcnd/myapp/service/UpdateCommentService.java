package com.example.jeffmcnd.myapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.example.jeffmcnd.myapp.Constants;
import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.model.Comment;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class UpdateCommentService extends IntentService {

    private int bevId;
    private int accountId;

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
            if (extras.containsKey(Constants.COMMENT_CONTENT_KEY) &&
                    extras.containsKey(Constants.BEVERAGE_ID_KEY) &&
                    extras.containsKey(Constants.ACCOUNT_ID_KEY)) {
                String content = extras.getString(Constants.COMMENT_CONTENT_KEY);
                bevId = extras.getInt(Constants.BEVERAGE_ID_KEY);
                accountId = extras.getInt(Constants.ACCOUNT_ID_KEY);
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

    public void deleteCommentFromRealm() {
        Realm realm = Constants.getRealmInstance(this);
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
