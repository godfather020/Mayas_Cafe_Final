package com.example.mayasfood;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseCloudMsg extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);
        Log.d("newToken", token);
        getSharedPreferences("DeviceToken", MODE_PRIVATE).edit().putString("fireBaseToken", token).apply();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        super.onMessageReceived(message);

    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("DeviceToken", MODE_PRIVATE).getString("fireBaseToken", "empty");
    }
}
