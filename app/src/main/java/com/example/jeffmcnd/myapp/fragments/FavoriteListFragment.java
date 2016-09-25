package com.example.jeffmcnd.myapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.models.Beverage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class FavoriteListFragment extends Fragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnFavoriteListItemClicked mListener;

    public FavoriteListFragment() {
    }

    public static FavoriteListFragment newInstance(int columnCount) {
        FavoriteListFragment fragment = new FavoriteListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new FavoriteRecyclerViewAdapter(new ArrayList<Beverage>(), mListener));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                .addConverterFactory(MoshiConverterFactory.create())
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
                }
            }

            @Override
            public void onFailure(Call<List<Beverage>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

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
}
