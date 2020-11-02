package com.techsalt.tclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techsalt.tclient.apiInterface.ApiInterface;
import com.techsalt.tclient.modelClass.clientMappedModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class siteDetail extends AppCompatActivity {
    String issue;
    @BindView(R.id.issueType)
    Spinner issueType;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.siteType)
    Spinner siteType;
    String issueGot, siteGot;
    int gotId;
    ArrayList<Integer> siteId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_detail);
        ButterKnife.bind(this);

        sharedPreferences = siteDetail.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        APP_SHARED_PREFS = "TClient";
        siteSpinner();
        issueSpinner();
        siteId = new ArrayList<Integer>();

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
                    for (int i = 0; i < response.body().getSiteDetails().size(); i++) {
                        Log.i("FOR LOOP ", "onResponse: " + response.body().getSiteDetails().get(i).getSiteName() + "----------------id " + response.body().getSiteDetails().get(i).getSiteId());
                        sites.add("\t     " + response.body().getSiteDetails().get(i).getSiteName() + "\t");
                        siteId.add(response.body().getSiteDetails().get(i).getSiteId());
                    }
                }
            }

            @Override
            public void onFailure(Call<clientMappedModel> call, Throwable t) {
                Toasty.error(siteDetail.this, t.getLocalizedMessage(), Toasty.LENGTH_LONG, true).show();
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

        siteType.setAdapter(adapter);

        siteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void issueSpinner() {
        ArrayList<String> issueTypes = new ArrayList<String>();
        issueTypes.add("\t\tSelect issue ");
        issueTypes.add("    \tInjury to employee or contractor");
        issueTypes.add("    \tQuality control");
        issueTypes.add("    \tLiability report");
        issueTypes.add("    \tProperty damage");
        issueTypes.add("    \tSecurity incident");
        issueTypes.add("    \tEnvironmental incident");
        issueTypes.add("    \tLost time injury");
        issueTypes.add("    \tFirst aid treatment");
        issueTypes.add("    \tDeath or serious incident");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, issueTypes) {


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

        issueType.setAdapter(adapter);

        issueType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    issueGot = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        if (siteGot == null || issueGot == null || siteGot.isEmpty() || issueGot.isEmpty() || issueGot.trim().equals("Select issue") || siteGot.trim().equals("Select site")) {
            Toasty.error(siteDetail.this, "Select Site/Issue", Toasty.LENGTH_LONG, true).show();
        } else {
            Intent i = new Intent(siteDetail.this, submitQuery.class);
            i.putExtra("siteGot", siteGot);
            i.putExtra("gotId", gotId);
            i.putExtra("issueGot", issueGot.trim());
            startActivity(i);
        }

    }
}