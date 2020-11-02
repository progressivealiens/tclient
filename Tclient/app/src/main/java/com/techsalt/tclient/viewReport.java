package com.techsalt.tclient;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techsalt.tclient.adapter.OperationalAbsentAdapter;
import com.techsalt.tclient.adapter.OperationalAttendanceAdapter;
import com.techsalt.tclient.apiInterface.ApiInterface;
import com.techsalt.tclient.modelClass.absenteseDataModel;
import com.techsalt.tclient.modelClass.attandanceDataModel;
import com.techsalt.tclient.modelClass.clientMappedModel;
import com.techsalt.tclient.utils.NetworkUtil;
import com.techsalt.tclient.utils.myProgressDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class viewReport extends AppCompatActivity implements View.OnClickListener {
    Toolbar tb;
    @BindView(R.id.siteSpinner)
    Spinner siteSpinner;
    ArrayList<Integer> siteId;
    String siteGot;
    int gotId = 0;
    @BindView(R.id.date_picker)
    TextView datePicker;
    @BindView(R.id.searchBT)
    Button searchBT;
    String parsedDate = "zero";
    @BindView(R.id.recycler_absent)
    RecyclerView recyclerAbsent;
    OperationalAbsentAdapter mAdapter;
    OperationalAttendanceAdapter mAdapter2;
    List<absenteseDataModel.Datum> absentList = new ArrayList<>();
    List<attandanceDataModel.Datum> attendanceList = new ArrayList<>();
    @BindView(R.id.viewAbsentee)
    Button viewAbsentee;
    myProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        ButterKnife.bind(this);
        tb = findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        siteId = new ArrayList<Integer>();
        sharedPreferences = viewReport.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        APP_SHARED_PREFS = "TClient";
        siteSpinner();
        datePicker.setOnClickListener(this);
        searchBT.setOnClickListener(this);
        viewAbsentee.setOnClickListener(this);


        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerAbsent.setLayoutManager(manager);

        mAdapter = new OperationalAbsentAdapter(this, absentList);
        mAdapter2 = new OperationalAttendanceAdapter(this, attendanceList);
        recyclerAbsent.setAdapter(mAdapter);
        recyclerAbsent.setAdapter(mAdapter2);

        progressDialog = new myProgressDialog(viewReport.this);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    private void siteSpinner() {
        ArrayList<String> sites = new ArrayList<String>();
        sites.add("\t\tSelect site");
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://www.trackkers.com/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<clientMappedModel> call = apiInterface.clientMappedSite(sharedPreferences.getString("TOKEN", ""));
        call.enqueue(new Callback<clientMappedModel>() {
            @Override
            public void onResponse(Call<clientMappedModel> call, Response<clientMappedModel> response) {
                Log.i("API ", "onResponse: RAW ----> " + response.raw());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        for (int i = 0; i < response.body().getSiteDetails().size(); i++) {
                            Log.i("FOR LOOP ", "onResponse: " + response.body().getSiteDetails().get(i).getSiteName() + "----------------id " + response.body().getSiteDetails().get(i).getSiteId());
                            sites.add("\t     " + response.body().getSiteDetails().get(i).getSiteName() + "\t");
                            siteId.add(response.body().getSiteDetails().get(i).getSiteId());
                        }
                    } else {
                        Toasty.error(viewReport.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                        Intent i = new Intent(viewReport.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<clientMappedModel> call, Throwable t) {
                Toasty.error(viewReport.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sites) {


            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }


        };

        siteSpinner.setAdapter(adapter);

        siteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    siteGot = (String) parent.getItemAtPosition(position);
                    gotId = siteId.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_picker:
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePick = new DatePickerDialog(viewReport.this, (vi, year, monthOfYear, dayOfMonth) -> {
                    DecimalFormat mFormat = new DecimalFormat("00");
                    try {
                        Date initDate = new SimpleDateFormat("dd/mm/yyyy").parse(mFormat.format(Double.valueOf(dayOfMonth)) + "/" + (monthOfYear + 1) + "/" + year);
                        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
                        parsedDate = formatter.format(initDate);
                        datePicker.setText(parsedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                cal.add(Calendar.YEAR, 0);
                datePick.getDatePicker().setMaxDate(cal.getTimeInMillis());
                datePick.show();
                break;
            case R.id.searchBT:
                if (gotId == 0 || parsedDate.equals("zero")) {
                    Toasty.error(viewReport.this, "Select Site/Date", Toasty.LENGTH_LONG, true).show();
                } else {
                    if (NetworkUtil.isOnline(viewReport.this)) {
                        progressDialog.show();
                        Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl("https://www.trackkers.com/api/")
                                .addConverterFactory(GsonConverterFactory.create());
                        Retrofit retrofit = builder.build();
                        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                        Call<attandanceDataModel> call = apiInterface.clientMobileAttendance(sharedPreferences.getString("TOKEN", ""), gotId, parsedDate);
                        call.enqueue(new Callback<attandanceDataModel>() {
                            @Override
                            public void onResponse(Call<attandanceDataModel> call, Response<attandanceDataModel> response) {
                                progressDialog.dismiss();
                                if (response.body().getStatus().equalsIgnoreCase("success")) {
                                    Log.i("ATTANDANE API ", "onResponse: " + response.body().getData());
                                    attendanceList.clear();
                                    attendanceList.addAll(response.body().getData());

                                    recyclerAbsent.setVisibility(View.VISIBLE);
                                    mAdapter2.notifyDataSetChanged();

                                } else {
                                    Toasty.error(viewReport.this, response.body().getMsg(), Toasty.LENGTH_LONG, true).show();
                                    recyclerAbsent.setVisibility(View.INVISIBLE);
                                }

                            }

                            @Override
                            public void onFailure(Call<attandanceDataModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.i("TAG ATTENDANCE API ", "onFailure: FAILURE " + t);
                                Toasty.error(viewReport.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();

                            }
                        });
                    } else {
                        Toasty.error(viewReport.this, "No internet connection", Toasty.LENGTH_LONG, true).show();
                    }
                }
                break;
            case R.id.viewAbsentee:
                if (gotId == 0 || parsedDate.equals("zero")) {
                    Toasty.error(viewReport.this, "Select Site/Date", Toasty.LENGTH_LONG, true).show();
                } else {
                    Intent i = new Intent(viewReport.this, ViewAbsentece.class);
                    i.putExtra("siteName", siteGot);
                    i.putExtra("gotId", String.valueOf(gotId));
                    i.putExtra("parsedDate", parsedDate);
                    startActivity(i);
                }
                break;
        }
    }
}