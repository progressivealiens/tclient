package com.techsalt.tclient.utils;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.techsalt.tclient.R;

public class myProgressDialog {
    private Activity activity;
    private AlertDialog dialog;

    public myProgressDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void show() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

    }

    public void dismiss() {
        dialog.dismiss();

    }
}
