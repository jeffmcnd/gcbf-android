package com.example.jeffmcnd.myapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.example.jeffmcnd.myapp.Constants;
import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.model.Beverage;
import com.example.jeffmcnd.myapp.model.Favorite;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class UpdateFavoriteService extends IntentService {

    int accountId;
    int bevId;

    public UpdateFavoriteService() {
        super("UpdateFavoriteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(Constants.BEVERAGE_ID_KEY) &&
                    extras.containsKey(Constants.ACCOUNT_ID_KEY) &&
                    extras.containsKey(Constants.SHOULD_POST_FAV)) {
                String content = extras.getString(Constants.COMMENT_CONTENT_KEY);
                bevId = extras.getInt(Constants.BEVERAGE_ID_KEY);
                accountId = extras.getInt(Constants.ACCOUNT_ID_KEY);
                boolean shouldPostFav = extras.getBoolean(Constants.SHOULD_POST_FAV);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build();

                GcbfService client = retrofit.create(GcbfService.class);
                Call<Beverage> favoriteCall = shouldPostFav ?
                        client.postFavorite(accountId, bevId) :
                        client.deleteFavorite(accountId, bevId);

                favoriteCall.enqueue(new Callback<Beverage>() {
                    @Override
                    public void onResponse(Call<Beverage> call, Response<Beverage> response) {
                        if (response.isSuccessful()) {
                            deleteFavoriteFromDb();
                        }
                    }

                    @Override
                    public void onFailure(Call<Beverage> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void deleteFavoriteFromDb() {
        Realm realm = Constants.getRealmInstance(this);
        final RealmResults<Favorite> results = realm.where(Favorite.class)
                .equalTo("beverageId", bevId)
                .equalTo("accountId", accountId)
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
