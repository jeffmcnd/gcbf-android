package com.example.jeffmcnd.myapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.fragment.BeverageFragment;
import com.example.jeffmcnd.myapp.model.Beverage;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeverageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_tv) TextView toolbarTextView;

    @InjectExtra Beverage beverage;
    public BeverageFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beverage);
        ButterKnife.bind(this);
        Dart.inject(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (beverage != null) {
            toolbarTextView.setText(beverage.name);
        }

        if (savedInstanceState == null) {
            fragment = BeverageFragment.newInstance(beverage);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        fragment.saveState();
        super.onBackPressed();
    }
}
