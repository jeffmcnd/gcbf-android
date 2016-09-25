package com.example.jeffmcnd.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class GcbfListFragment extends Fragment {

    public boolean downloading = false;
    public boolean loaded = false;
    NetworkChangeBroadcastReceiver receiver = new NetworkChangeBroadcastReceiver();

    public GcbfListFragment() { }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void loadData() {
        throw new UnsupportedOperationException("loadData not implemeneted!");
    }

    public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if(!extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY) && !downloading && !loaded) {
                    loadData();
                }
            }
        }
    }
}
