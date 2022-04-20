package com.example.mayasfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.BoringLayout;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.example.mayasfood.activity.OTP;
import com.example.mayasfood.constants.Constants;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String title = Objects.requireNonNull(message.getNotification()).getTitle();
        String body = message.getNotification().getBody();
        String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

        Log.d("msgbody", title);

        if (body.contains("OTP")) {

            OTP.getOtpFromMessage(body);
        }

        NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Heads up notification",
                        NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new Notification.BigTextStyle())
                .setSmallIcon(R.drawable.mayas512__1_)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification.build());

        super.onMessageReceived(message);

    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("DeviceToken", MODE_PRIVATE).getString("fireBaseToken", "empty");
    }
}
