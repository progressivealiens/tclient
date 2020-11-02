package com.techsalt.tclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String token, companyNameS, clientEmailS, clientCodeS;
    @BindView(R.id.company_Name)
    TextInputEditText companyName;
    @BindView(R.id.client_Email)
    TextInputEditText clientEmail;
    @BindView(R.id.client_CodeET)
    TextInputEditText clientCodeET;
    @BindView(R.id.submitBT)
    Button submitBT;
    Toolbar loginToolbar;
    myProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getFirebaseMessagingToken();
        submitBT.setOnClickListener(this);


        loginToolbar = findViewById(R.id.loginToolBar);
        setSupportActionBar(loginToolbar);
        sharedPreferences = LoginActivity.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        APP_SHARED_PREFS = "TClient";
        progressDialog = new myProgressDialog(LoginActivity.this);
        if (sharedPreferences.getString("STATUS", "").isEmpty()) {

        } else {
            sendToDashboard();
        }
    }

    private void getFirebaseMessagingToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String TAG = "Firebase Token";
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        editor.putString("TOKEN", token);
                        editor.commit();
                        // Log and toast
                        Log.i(TAG, "Token : " + token);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBT:
                if (NetworkUtil.isOnline(this)) {
                    clientMobileLoginAPI();
                } else {
                    Toasty.error(LoginActivity.this, "No Internet Connection", Toasty.LENGTH_LONG, true).show();
                }
                break;
        }
    }


    private void sendToDashboard() {
        Log.i("INTENT SENT TO", "sendToDashboard: Intent ");

        Intent i = new Intent(LoginActivity.this, Dashboard.class);
        startActivity(i);
        finish();
    }

    private void clientMobileLoginAPI() {
        companyNameS = companyName.getText().toString().trim();
        clientCodeS = clientCodeET.getText().toString().trim();
        clientEmailS = clientEmail.getText().toString().trim();

        if (companyNameS.length() > 4 || clientEmailS.length() > 4 || clientCodeS.length() > 4) {
            progressDialog.show();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://www.trackkers.com/api/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<model> call = apiInterface.clientMobileLogin(token, companyNameS, clientEmailS, clientCodeS);
            call.enqueue(new Callback<model>() {
                String TAG = "API ";

                @Override
                public void onResponse(Call<model> call, Response<model> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            Log.i(TAG, "onResponse: RAW " + response.raw());
                            editor.putString("MESSAGE", response.body().getMsg());
                            editor.putString("STATUS", response.body().getStatus());
                            editor.putString("COMPANY_LOGO", response.body().getCompanyLogo());
                            editor.putString("Client_NAME", response.body().getClientName());
                            editor.putString("COMPANY_NAME", response.body().getCompanyName());
                            editor.putString("CLIENT_ID", String.valueOf(response.body().getClientId()));
                            Log.i(TAG, "onResponse: RAW " + response.raw());
                            editor.commit();
                            Toasty.success(LoginActivity.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                            sendToDashboard();
                        } else {
                            Toasty.error(LoginActivity.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                            Log.i(TAG, "onResponse: RAW " + response.raw());
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<model> call, Throwable t) {
                    Log.i(TAG, "onFailure: onFailure " + t.getLocalizedMessage());
                    progressDialog.dismiss();
                    Toasty.error(LoginActivity.this, t.getMessage(), Toasty.LENGTH_LONG, true).show();
                }
            });
        } else {
            Toasty.error(this, "Invalid Input", Toast.LENGTH_LONG, true).show();
        }
    }

}