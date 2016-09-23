package com.example.jeffmcnd.myapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.MainActivity;
import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.models.Beverage;
import com.example.jeffmcnd.myapp.models.Comment;
import com.f2prateek.dart.InjectExtra;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BeverageFragment extends Fragment {
    @BindView(R.id.bev_id_tv) TextView bevIdTextView;
    @BindView(R.id.name_tv) TextView bevNameTextView;
    @BindView(R.id.blurb_tv) TextView bevBlurbTextView;
    @BindView(R.id.brewer_id_tv) TextView brewerIdTextView;
    @BindView(R.id.comment_et) EditText commentEditText;
    @BindView(R.id.image) ImageView imageView;

    @InjectExtra Beverage beverage;

    private OnBeverageFragmentBackClicked listener;

    public BeverageFragment() {
    }

    public static BeverageFragment newInstance(Beverage beverage) {
        BeverageFragment fragment = new BeverageFragment();
        fragment.beverage = beverage;
        Bundle args = new Bundle();
        args.putParcelable("beverage", beverage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Dart.inject(beverage, savedInstanceState);

        Bundle args = getArguments();
        beverage = args.getParcelable("beverage");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beverage, container, false);
        ButterKnife.bind(this, view);


        bevIdTextView.setText(String.valueOf(beverage.id));
        bevNameTextView.setText(beverage.name);
        bevBlurbTextView.setText(beverage.blurb == null ? "" : beverage.blurb);
        brewerIdTextView.setText(String.valueOf(beverage.brewer_id));
        Picasso.with(getContext())
                .load(beverage.imageurl == null ? "http://placekitten.com/200/300" : beverage.imageurl)
                .into(imageView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        GcbfService client = retrofit.create(GcbfService.class);
        Call<Comment> getCommentCall = client.getComment(1, beverage.id);

        getCommentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment comment = response.body();
                    commentEditText.setText(comment.content);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onBeverageFragmentBackClicked();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBeverageFragmentBackClicked) {
            listener = (OnBeverageFragmentBackClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnBeverageFragmentBackClicked {
        void onBeverageFragmentBackClicked();
    }
}
