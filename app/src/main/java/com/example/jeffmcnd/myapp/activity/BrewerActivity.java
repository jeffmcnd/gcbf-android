package com.example.jeffmcnd.myapp.activity;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.R;

import com.example.jeffmcnd.myapp.models.Beverage;
import com.example.jeffmcnd.myapp.models.Booth;
import com.example.jeffmcnd.myapp.models.Brewer;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrewerActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_tv) TextView toolbarTextView;
    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.location) TextView locationTextView;
    @BindView(R.id.booths) TextView boothTextView;
    @BindView(R.id.bevs) TextView bevTextView;

    @InjectExtra Brewer brewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brewer);
        Dart.inject(this);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (brewer != null) {
            toolbarTextView.setText(brewer.name);
            nameTextView.setText(brewer.name);
            locationTextView.setText(brewer.location);

            String boothString = "";
            for(Booth booth : brewer.booths) {
                boothString = boothString.concat(booth.toString());
            }
            boothTextView.setText(boothString);

            String bevString = "";
            for(Beverage bev : brewer.beverages) {
                bevString = bevString.concat(bev.toString());
            }
            bevTextView.setText(bevString);
        } else {
            toolbarTextView.setText(getString(R.string.app_name));
        }
    }
}
