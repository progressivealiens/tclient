package com.techsalt.tclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class splashScreen extends AppCompatActivity {
    String token;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String APP_SHARED_PREFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
       /* if(NetworkUtil.isOnline(splashScreen.this)){
            getFirebaseMessagingToken();
        }*/
        APP_SHARED_PREFS = "TClient";
        sharedPreferences = splashScreen.this.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Objects.requireNonNull(sharedPreferences.getString("STATUS", "")).isEmpty()) {
                    Intent i = new Intent(splashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    Intent i = new Intent(splashScreen.this, Dashboard.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);
    }

    /*private void getFirebaseMessagingToken() {
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
    }*/
}