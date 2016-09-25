package com.example.jeffmcnd.myapp;

import com.example.jeffmcnd.myapp.model.Account;
import com.example.jeffmcnd.myapp.model.Beverage;
import com.example.jeffmcnd.myapp.model.Brewer;
import com.example.jeffmcnd.myapp.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GcbfService {
    @GET("brewers")
    Call<List<Brewer>> listBrewers();

    @GET("brewers/{brewer_id}")
    Call<Brewer> getBrewer(@Path("brewer_id") int brewerId);

    @GET("beverages")
    Call<List<Beverage>> listBeverages();

    @GET("beverages/{bev_id}")
    Call<Beverage> getBeverage(@Path("bev_id") int bevId);

    @GET("favorites")
    Call<List<Beverage>> listFavorites();

    @GET("favorites/{user_id}")
    Call<List<Beverage>> listUserFavorites(@Path("user_id") int userId);

    @GET("comments/{user_id}/{bev_id}")
    Call<Comment> getComment(@Path("user_id") int userId,
                             @Path("bev_id") int bevId);

    @FormUrlEncoded
    @POST("comments/{user_id}/{bev_id}")
    Call<Comment> postComment(@Path("user_id") int userId,
                              @Path("bev_id") int bevId,
                              @Field("content") String content);

    @FormUrlEncoded
    @POST("accounts")
    Call<Account> postAccount(@Field("name") String name);
}
