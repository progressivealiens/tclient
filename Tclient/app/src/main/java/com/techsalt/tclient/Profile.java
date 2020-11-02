package com.techsalt.tclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.techsalt.tclient.apiInterface.ApiInterface;
import com.techsalt.tclient.modelClass.clientProfileModelClass;
import com.techsalt.tclient.utils.NetworkUtil;
import com.techsalt.tclient.utils.myProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.designationTextView)
    TextView designationTextView;
    @BindView(R.id.codeTextView)
    TextView codeTextView;
    @BindView(R.id.emailTextView)
    TextView emailTextView;
    @BindView(R.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R.id.addressTextView)
    TextView addressTextView;
    myProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPreferences = Profile.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        APP_SHARED_PREFS = "TClient";
        progressDialog = new myProgressDialog(Profile.this);
        if (NetworkUtil.isOnline(Profile.this)) {
            clientProfileAPI();
        } else {
            Toasty.error(Profile.this, "NO Internet Connection", Toasty.LENGTH_LONG, true).show();
        }

    }

    private void clientProfileAPI() {
        progressDialog.show();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://www.trackkers.com/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<clientProfileModelClass> call = apiInterface.clientProfile(sharedPreferences.getString("TOKEN", ""));
        call.enqueue(new Callback<clientProfileModelClass>() {
            @Override
            public void onResponse(Call<clientProfileModelClass> call, Response<clientProfileModelClass> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        nameTextView.setText(response.body().getName());
                        designationTextView.setText(response.body().getDesignation());
                        codeTextView.setText(response.body().getCode());
                        emailTextView.setText(response.body().getEmail());
                        mobileTextView.setText(response.body().getMobile());
                        addressTextView.setText(response.body().getAddress());
                    } else {
                        Toasty.error(Profile.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<clientProfileModelClass> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(Profile.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}