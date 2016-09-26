package com.example.jeffmcnd.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.Constants;
import com.example.jeffmcnd.myapp.GcbfService;
import com.example.jeffmcnd.myapp.MainActivity;
import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.model.Account;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccountActivity extends AppCompatActivity {

    @BindView(R.id.account_name_et) EditText nameEditText;
    @BindView(R.id.error_tv) TextView errorTextView;
    @BindView(R.id.submit_btn) Button button;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        Realm realm = Constants.getRealmInstance(this);

        final RealmResults<Account> results = realm.where(Account.class).findAll();
        if (results.size() > 0) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    results.deleteAllFromRealm();
//                }
//            });
//
//            final RealmResults<Comment> commentResults = realm.where(Comment.class).findAll();
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    results.deleteAllFromRealm();
//                }
//            });
            startMainActivity();
        }

    }

    @OnClick(R.id.submit_btn)
    public void submitButtonClicked() {
        String name = nameEditText.getText().toString();
        if (name.length() < 4) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(getString(R.string.error_name_short));
        } else if (name.contains(" ")) {
            errorTextView.setText(getString(R.string.error_name_space));
            errorTextView.setVisibility(View.VISIBLE);
        } else {
            button.setEnabled(false);
            errorTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            createAccount(name);
        }
    }

    public void createAccount(String name) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gcbf.mcnallydawes.xyz:8000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GcbfService client = retrofit.create(GcbfService.class);
        Call<Account> createAccountCall = client.postAccount(name);

        createAccountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Account account = response.body();
                    if (account.error != null) {
                        errorTextView.setText(account.error);
                        errorTextView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                    } else {
                        saveAccount(account);
                        startMainActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                errorTextView.setText(getString(R.string.error_connect_fail));
                errorTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
            }
        });
    }


    public void saveAccount(Account account) {
        Realm realm = Constants.getRealmInstance(this);
        realm.beginTransaction();
        realm.copyToRealm(account);
        realm.commitTransaction();
        realm.close();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
