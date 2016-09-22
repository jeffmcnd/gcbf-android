package com.example.jeffmcnd.myapp;

import com.example.jeffmcnd.myapp.models.Beverage;
import com.example.jeffmcnd.myapp.models.Brewer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GcbfService {
    @GET("brewers")
    Call<List<Brewer>> listBrewers();

    @GET("beverages")
    Call<List<Beverage>> listBeverages();

    @GET("favorites")
    Call<List<Beverage>> listFavorites();
}
