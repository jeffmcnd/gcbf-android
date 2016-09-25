package com.example.jeffmcnd.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.model.Brewer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrewerFragment extends Fragment {
    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.location) TextView locationTextView;
    @BindView(R.id.booths) TextView boothTextView;
    @BindView(R.id.bevs) TextView bevTextView;

    Brewer brewer;

    public BrewerFragment() { }

    public static BrewerFragment newInstance(Brewer brewer) {
        BrewerFragment fragment = new BrewerFragment();
        fragment.brewer = brewer;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brewer, container, false);
        ButterKnife.bind(this, view);

        nameTextView.setText(brewer.name);
        locationTextView.setText(brewer.location);
        boothTextView.setText(TextUtils.join(", ", brewer.booths));
        bevTextView.setText(TextUtils.join(", ", brewer.beverages));

        return view;
    }

}
