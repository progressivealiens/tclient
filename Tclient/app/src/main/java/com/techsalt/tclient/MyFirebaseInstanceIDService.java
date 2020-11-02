package com.techsalt.tclient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    /*AppSharePreference appSharePreferenced;
    private Context context;*/

   /* @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        *//*context = this;*//*
//        appSharePreferenced = new AppSharePreference(context);
        *//* String token = FirebaseInstanceId.getInstance().getToken();*//*
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
//                appSharePreferenced.setFirebaseToken(token);
            }
        });

    }*/

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_error_outline_white_24dp)
                .setAutoCancel(true);

        Intent intent = new Intent(this, splashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = null;

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }
}
