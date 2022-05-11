package com.example.mayasfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirebaseCloudMsg extends FirebaseMessagingService {

    public static EditText otp1,otp2,otp3,otp4,otp5,otp6;

    public void setEditTextOtp(EditText editText1, EditText editText2, EditText editText3, EditText editText4, EditText editText5, EditText editText6){
        FirebaseCloudMsg.otp1 = editText1;
        FirebaseCloudMsg.otp2 = editText2;
        FirebaseCloudMsg.otp3 = editText3;
        FirebaseCloudMsg.otp4 = editText4;
        FirebaseCloudMsg.otp5 = editText5;
        FirebaseCloudMsg.otp6 = editText6;
    }

    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);
        Log.d("newToken", token);
        getSharedPreferences("MsgToken", MODE_PRIVATE).edit().putString("fireBaseToken", token).apply();

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

        if (body.contains("verification")) {

            Pattern pattern = Pattern.compile("(|^)\\d{6}");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                Log.d("Otp", matcher.group(0));
                String otp = matcher.group(0);
                //setOtp(matcher.group(0));
                String sotp1 = otp.substring(0,1);
                Log.d("o1", sotp1);
                String sotp2 = otp.substring(1,2);
                Log.d("o2", sotp2);
                String sotp3 = otp.substring(2,3);
                Log.d("o3", sotp3);
                String sotp4 = otp.substring(3,4);
                Log.d("o4", sotp4);
                String sotp5 = otp.substring(4,5);
                Log.d("o5", sotp5);
                String sotp6 = otp.substring(5);
                Log.d("o6", sotp6);

                otp1.setText(sotp1);

                otp2.setText(sotp2);

                otp3.setText(sotp3);

                otp4.setText(sotp4);

                otp5.setText(sotp5);

                otp6.setText(sotp6);

                Log.d("Complete", "working");
            }
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
                .setSmallIcon(R.drawable.mayas512__1_svg)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification.build());

        super.onMessageReceived(message);

    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("MsgToken", MODE_PRIVATE).getString("fireBaseToken", "empty");
    }
}
