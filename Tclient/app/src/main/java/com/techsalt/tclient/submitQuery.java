package com.techsalt.tclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.techsalt.tclient.apiInterface.ApiInterface;
import com.techsalt.tclient.modelClass.model;
import com.techsalt.tclient.utils.NetworkUtil;
import com.techsalt.tclient.utils.myProgressDialog;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class submitQuery extends AppCompatActivity {
    Toolbar tb2;
    @BindView(R.id.employee_name)
    TextInputEditText employeeName;
    @BindView(R.id.message)
    TextInputEditText message;
    @BindView(R.id.cameraBT)
    ImageView cameraBT;
    @BindView(R.id.submitBT)
    Button submitBT;

    String employee_Name;
    String msg;

    String subject;
    String image;
    Bitmap photo;
    @BindView(R.id.subject)
    TextInputEditText subjectET;
    String issueGot, siteGot;
    int gotId;
    myProgressDialog progressDialog;
    @BindView(R.id.SiteTV)
    TextView SiteTV;
    @BindView(R.id.issueTV)
    TextView issueTV;
    private SharedPreferences sharedPreferences;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_query);
        ButterKnife.bind(this);
        tb2 = findViewById(R.id.toolbar3);
        setSupportActionBar(tb2);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPreferences = submitQuery.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        APP_SHARED_PREFS = "TClient";

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            siteGot = extras.getString("siteGot");
            issueGot = extras.getString("issueGot");
            gotId = extras.getInt("gotId");
            SiteTV.append(siteGot.trim());
            issueTV.append(issueGot.trim());
        }
        progressDialog = new myProgressDialog(submitQuery.this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("CheckResult")
    @OnClick({R.id.cameraBT, R.id.submitBT})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cameraBT:

                AlertDialog.Builder builder = new AlertDialog.Builder(submitQuery.this);
                View v = getLayoutInflater().inflate(R.layout.upload_dialog, null);
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                ImageButton camera = v.findViewById(R.id.cameraButton);
                ImageButton gallery = v.findViewById(R.id.galleryButton);

                camera.setOnClickListener(view1 -> {
                    alertDialog.dismiss();
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera_intent, 100);
                });
                gallery.setOnClickListener(view12 -> {
                    alertDialog.dismiss();
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Image"), 101);

                });
                break;
            case R.id.submitBT:
                employee_Name = employeeName.getText().toString().trim();
                msg = message.getText().toString().trim();
                subject = subjectET.getText().toString().trim();

                if (subject.isEmpty()) {
                    subjectET.setError("");
                } else {
                    if (employee_Name.isEmpty()) {
                        employeeName.setError("");
                    } else {
                        if (msg.isEmpty()) {
                            message.setError("");
                        } else {

                            if (NetworkUtil.isOnline(this)) {
                                clientSubmitSiteIssueMailAPI();
                            } else {
                                Toasty.error(this, "No internet connection", Toasty.LENGTH_LONG, true).show();
                            }
                        }
                    }

                }
                break;
        }
    }

    private void clientSubmitSiteIssueMailAPI() {
        progressDialog.show();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://www.trackkers.com/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<model> call = apiInterface.clientSubmitSiteIssueMail(sharedPreferences.getString("TOKEN", ""), gotId, issueGot, subject, employee_Name, msg, image);
        call.enqueue(new Callback<model>() {

            @Override
            public void onResponse(Call<model> call, Response<model> response) {
                progressDialog.dismiss();
                Log.i("API ", "onResponse: RAW " + response.raw());
                if (response.body().getStatus().equals("success")) {
                    Intent i = new Intent(submitQuery.this, Dashboard.class);
                    startActivity(i);
                    finish();
                    Toasty.success(submitQuery.this, "Query Submitted Sucessfully", Toasty.LENGTH_LONG, true).show();
                    Log.i("API", "onResponse: Message --> " + response.body().getMsg());
                } else {
                    Toasty.error(submitQuery.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<model> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(submitQuery.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                photo = (Bitmap) data.getExtras().get("data");
                cameraBT.setImageBitmap(photo);

            } else {
                Toasty.error(submitQuery.this, "Try Again", Toasty.LENGTH_LONG, true).show();
            }
        } else if (requestCode == 101) {
            if (data != null) {
                Uri uri = data.getData();

                cameraBT.setImageURI(uri);
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toasty.error(submitQuery.this, "Try Again", Toasty.LENGTH_LONG, true).show();
            }
        }

    }
}
