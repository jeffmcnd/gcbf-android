package com.example.jeffmcnd.myapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.models.Beverage;

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
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BeverageListFragment extends Fragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_layout) LinearLayout errorLayout;

    private OnListFragmentInteractionListener listener;
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
    private boolean downloading = false;

    public BeverageListFragment() {
    }

    @SuppressWarnings("unused")
    public static BeverageListFragment newInstance(int columnCount) {
        BeverageListFragment fragment = new BeverageListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beverage_list, container, false);
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
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkChangeReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if(!extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY) && recyclerView.getAdapter() == null && !downloading) {
                    loadData();
                }
            }
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Beverage bev);
    }

    public void loadData() {
        downloading = true;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        GcbfService client = retrofit.create(GcbfService.class);

        Call<List<Beverage>> listBeveragesCall = client.listBeverages();

        listBeveragesCall.enqueue(new Callback<List<Beverage>>() {
            @Override
            public void onResponse(Call<List<Beverage>> call, Response<List<Beverage>> response) {
                if (response.isSuccessful()) {
                    List<Beverage> bevs = response.body();
                    recyclerView.setAdapter(new BeverageRecyclerViewAdapter(bevs, listener));
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
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
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        loadData();
    }
}
