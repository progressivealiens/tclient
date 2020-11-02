package com.techsalt.tclient;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class issueType extends AppCompatActivity {
    String issue;
    @BindView(R.id.issueType)
    Spinner issueType;
    @BindView(R.id.button)
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_type);
        ButterKnife.bind(this);


    }


}