package com.techsalt.tclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.techsalt.tclient.apiInterface.ApiInterface;
import com.techsalt.tclient.modelClass.model;
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

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    Button viewReport, submitQuery;
    @BindView(R.id.clientNameTV)
    TextView clientNameTV;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String TAG = "Logout API ";
    myProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        progressDialog = new myProgressDialog(Dashboard.this);
        sharedPreferences = Dashboard.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        APP_SHARED_PREFS = "TClient";
        setSupportActionBar(toolbar);

        Picasso.get().load("https://www.trackkers.com/uploads/companyLogo/" + sharedPreferences.getString("COMPANY_LOGO", "")).resize(150, 150).into(logo);

        viewReport = findViewById(R.id.viewReport);
        submitQuery = findViewById(R.id.submitQuery);

        viewReport.setOnClickListener(this);
        submitQuery.setOnClickListener(this);
        clientNameTV.append(sharedPreferences.getString("Client_NAME", ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitQuery:
                Intent i = new Intent(Dashboard.this, siteDetail.class);
                startActivity(i);
                break;
            case R.id.viewReport:
                Intent i2 = new Intent(Dashboard.this, viewReport.class);
                startActivity(i2);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                if (NetworkUtil.isOnline(Dashboard.this)) {
                    progressDialog.show();
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl("https://www.trackkers.com/api/")
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit = builder.build();
                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                    Call<model> call = apiInterface.clientMobileLogout(sharedPreferences.getString("TOKEN", ""));
                    call.enqueue(new Callback<model>() {
                        @Override
                        public void onResponse(Call<model> call, Response<model> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equals("success")) {
                                    editor.clear();
                                    editor.commit();
                                    Log.i(TAG, "onResponse: RAW " + response.raw());
                                    Toasty.success(Dashboard.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                                    sendToLoginActivity();
                                } else {
                                    editor.clear();
                                    editor.commit();
                                    Toast.makeText(Dashboard.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Dashboard.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                Toasty.error(Dashboard.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<model> call, Throwable t) {
                            progressDialog.dismiss();
                            Toasty.error(Dashboard.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();
                        }
                    });
                } else {
                    Toasty.error(Dashboard.this, "No Internet Connection", Toasty.LENGTH_LONG, true).show();
                }

                break;
            case R.id.Profile:
                Intent i = new Intent(Dashboard.this, Profile.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendToLoginActivity() {
        Intent i = new Intent(Dashboard.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}