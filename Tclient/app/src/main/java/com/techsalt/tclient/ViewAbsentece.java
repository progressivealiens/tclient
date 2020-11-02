package com.techsalt.tclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techsalt.tclient.adapter.OperationalAbsentAdapter;
import com.techsalt.tclient.apiInterface.ApiInterface;
import com.techsalt.tclient.modelClass.absenteseDataModel;
import com.techsalt.tclient.utils.NetworkUtil;
import com.techsalt.tclient.utils.myProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewAbsentece extends AppCompatActivity {
    String gotId, parsedDate, siteName;
    @BindView(R.id.recycler_ViewAbsentece)
    RecyclerView recyclerViewAbsentece;
    List<absenteseDataModel.Datum> absentList = new ArrayList<>();
    OperationalAbsentAdapter mAdapter;
    @BindView(R.id.ViewAbsenteeToolbar)
    Toolbar ViewAbsenteeToolbar;
    myProgressDialog progressDialog;
    @BindView(R.id.siteTextView)
    TextView siteTextView;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
    private SharedPreferences sharedPreferences;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_absentece);
        ButterKnife.bind(this);
        setSupportActionBar(ViewAbsenteeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedPreferences = ViewAbsentece.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        APP_SHARED_PREFS = "TClient";

        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerViewAbsentece.setLayoutManager(manager);

        mAdapter = new OperationalAbsentAdapter(this, absentList);
        recyclerViewAbsentece.setAdapter(mAdapter);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            siteName = extras.getString("siteName");
            gotId = extras.getString("gotId");
            parsedDate = extras.getString("parsedDate");
            siteTextView.setText(siteName);
            dateTextView.setText(parsedDate);
        }
        progressDialog = new myProgressDialog(ViewAbsentece.this);
        recycler();


    }

    private void recycler() {
        if (NetworkUtil.isOnline(ViewAbsentece.this)) {
            progressDialog.show();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://www.trackkers.com/api/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<absenteseDataModel> call = apiInterface.clientMobileAbsentAttendance(sharedPreferences.getString("TOKEN", ""), gotId, parsedDate);
            call.enqueue(new Callback<absenteseDataModel>() {
                @Override
                public void onResponse(Call<absenteseDataModel> call, Response<absenteseDataModel> response) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        absentList.clear();
                        absentList.addAll(response.body().getData());
                        //tvEmpty.setVisibility(View.GONE);
                        Log.i("SUCCESS TAG", "onResponse: data " + response.body().getData());
                        recyclerViewAbsentece.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toasty.error(ViewAbsentece.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                    }

                }

                @Override
                public void onFailure(Call<absenteseDataModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.i("TAG ATTENDANCE API ", "onFailure: FAILURE " + t);
                    Toasty.error(ViewAbsentece.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();

                }
            });
        } else {
            Toasty.error(ViewAbsentece.this, "No internet connection", Toasty.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}