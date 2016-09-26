package com.example.jeffmcnd.myapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.jeffmcnd.myapp.GcbfListFragment;
import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.model.Beverage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteListFragment extends GcbfListFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_layout) LinearLayout errorLayout;

    private OnFavoriteListItemClicked mListener;

    public FavoriteListFragment() { }

    public static FavoriteListFragment newInstance(int columnCount) {
        FavoriteListFragment fragment = new FavoriteListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(null);

        loadData();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteListItemClicked) {
            mListener = (OnFavoriteListItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFavoriteListItemClicked {
        void onFavoriteListItemClicked(Beverage bev);
    }

    @Override
    public void loadData() {
        downloading = true;

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GcbfService client = retrofit.create(GcbfService.class);
        Call<List<Beverage>> listBeveragesCall = client.listFavorites();

        listBeveragesCall.enqueue(new Callback<List<Beverage>>() {
            @Override
            public void onResponse(Call<List<Beverage>> call, Response<List<Beverage>> response) {
                if (response.isSuccessful()) {
                    List<Beverage> beverages = response.body();
                    recyclerView.setAdapter(new FavoriteRecyclerViewAdapter(beverages, mListener));
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    loaded = true;
                }
                downloading = false;
            }

            @Override
            public void onFailure(Call<List<Beverage>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                downloading = false;
            }
        });
    }

    @OnClick(R.id.retry_btn)
    public void retryButtonClicked() {
        loadData();
    }
}
