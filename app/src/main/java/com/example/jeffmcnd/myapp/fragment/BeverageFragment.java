package com.example.jeffmcnd.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.Constants;
import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.model.Account;
import com.example.jeffmcnd.myapp.model.Beverage;
import com.example.jeffmcnd.myapp.model.Comment;
import com.example.jeffmcnd.myapp.model.Favorite;
import com.example.jeffmcnd.myapp.service.UpdateCommentService;
import com.example.jeffmcnd.myapp.service.UpdateFavoriteService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BeverageFragment extends Fragment {

    public static final String BEVERAGE_TAG = "beverage";

    @BindView(R.id.bev_id_tv) TextView bevIdTextView;
    @BindView(R.id.name_tv) TextView bevNameTextView;
    @BindView(R.id.blurb_tv) TextView bevBlurbTextView;
    @BindView(R.id.brewer_id_tv) TextView brewerIdTextView;
    @BindView(R.id.comment_et) EditText commentEditText;
    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.favorite_btn) Button favoriteButton;

    private Beverage beverage;
    private Account account;
    private String content;
    private boolean favorited = false;
    private boolean commentLoadSuccessful = false;
    private boolean favoriteLoadSuccessful = false;

    public BeverageFragment() { }

    public static BeverageFragment newInstance(Beverage beverage) {
        BeverageFragment fragment = new BeverageFragment();
        Bundle args = new Bundle();
        args.putParcelable(BEVERAGE_TAG, beverage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(BEVERAGE_TAG)) {
                beverage = args.getParcelable(BEVERAGE_TAG);
            }
        }

        Realm realm = Constants.getRealmInstance(getActivity());
        final RealmResults<Account> results = realm.where(Account.class).findAll();
        account = results.first();
        realm.close();
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
                .load(beverage.imageurl == null ? "http://placekitten.com/300/200" : beverage.imageurl)
                .into(imageView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        getComment(retrofit);
        getFavorite(retrofit);

        return view;
    }

    private void getComment(Retrofit retrofit) {
        GcbfService client = retrofit.create(GcbfService.class);
        Call<Comment> getCommentCall = client.getComment(account.id, beverage.id);

        getCommentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment comment = response.body();
                    commentEditText.setText(comment.content);
                    content = comment.content;
                    commentLoadSuccessful = true;
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void getFavorite(Retrofit retrofit) {
        GcbfService client = retrofit.create(GcbfService.class);
        final Call<Beverage> getFavoriteCall = client.getFavorite(account.id, beverage.id);

        getFavoriteCall.enqueue(new Callback<Beverage>() {
            @Override
            public void onResponse(Call<Beverage> call, Response<Beverage> response) {
                if (response.isSuccessful()) {
                    Beverage bev = response.body();
                    favoriteButton.setText(getString(bev.error == null ? R.string.button_unfavorite : R.string.button_favorite));
                    favorited = bev.error == null;
                    favoriteLoadSuccessful = true;
                }
            }

            @Override
            public void onFailure(Call<Beverage> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.favorite_btn)
    public void favoriteButtonClicked() {
        if (favoriteButton.getText().toString().equals(getString(R.string.button_unfavorite))) {
            favoriteButton.setText(getString(R.string.button_favorite));
        } else {
            favoriteButton.setText(getString(R.string.button_unfavorite));
        }

    }

    public void saveState() {
        saveComment();
        saveFavorite();
    }

    private void saveComment() {
        String currentContent = commentEditText.getText().toString();
        if(commentLoadSuccessful) {
            if ((content == null && !currentContent.equals("")) || (content != null && !content.equals(currentContent))) {
                Realm realm = Constants.getRealmInstance(getActivity());

                Comment comment = new Comment(-1, currentContent, beverage.id, account.id, null);

                final RealmResults<Comment> results = realm.where(Comment.class)
                                                           .equalTo("beverage_id", beverage.id)
                                                           .equalTo("user_id", account.id)
                                                           .findAll();
                realm.beginTransaction();

                if (results.size() > 0) {
                    comment = results.first();
                    comment.content = currentContent;
                }

                realm.copyToRealm(comment);
                realm.commitTransaction();
                realm.close();

                Intent intent = new Intent(getActivity(), UpdateCommentService.class);
                intent.putExtra(Constants.COMMENT_CONTENT_KEY, currentContent);
                intent.putExtra(Constants.BEVERAGE_ID_KEY, beverage.id);
                intent.putExtra(Constants.ACCOUNT_ID_KEY, account.id);
                getActivity().startService(intent);
            }
        }
    }

    private void saveFavorite() {
        boolean currentlyFavorited = favoriteButton.getText().toString().equals(getString(R.string.button_unfavorite));
        if (favoriteLoadSuccessful && currentlyFavorited != favorited) {
            Realm realm = Constants.getRealmInstance(getActivity());

            Favorite fav = new Favorite(-1, account.id, beverage.id);

            final RealmResults<Favorite> results = realm.where(Favorite.class)
                    .equalTo("accountId", account.id)
                    .equalTo("beverageId", beverage.id)
                    .findAll();
            if(results.size() > 0) {
                fav = results.first();
            } else {
                realm.beginTransaction();
                realm.copyToRealm(fav);
                realm.commitTransaction();
                realm.close();
            }

            Intent intent = new Intent(getActivity(), UpdateFavoriteService.class);
            intent.putExtra(Constants.ACCOUNT_ID_KEY, account.id);
            intent.putExtra(Constants.BEVERAGE_ID_KEY, beverage.id);
            intent.putExtra(Constants.SHOULD_POST_FAV, currentlyFavorited);
            getActivity().startService(intent);
        }
    }
}
