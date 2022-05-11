package com.example.mayasfood

import android.app.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.os.Build
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.example.mayasfood.R
import androidx.core.app.NotificationManagerCompat
import com.example.mayasfood.FirebaseCloudMsg
import android.widget.EditText
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

class FirebaseCloudMsg : FirebaseMessagingService() {


    override fun onNewToken(token: String) {

        //super.onNewToken(token);
        Log.d("newToken", token)
        getSharedPreferences("MsgToken", MODE_PRIVATE).edit().putString("fireBaseToken", token)
            .apply()

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.notification!!.title.toString()
        val body = message.notification!!.body.toString()
        val img = message.notification!!.imageUrl
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"

        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Heads up notification",
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        try {
            val url = URL(img.toString())
            var image = BitmapFactory.decodeStream(url.openConnection().getInputStream())


        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: Notification.Builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(Notification.BigTextStyle())
            .setSmallIcon(R.drawable.mayas512__1_)
            .setStyle(Notification.BigPictureStyle().bigPicture(image))
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(Random().nextInt(), notification.build())

        } catch (e: IOException) {
            println(e)
        }

        Log.d("msgbody", title)
        Log.d("msgbody", body)

        if (body.contains("OTP")) {
            val pattern = Pattern.compile("(|^)\\d{6}")
            val matcher = pattern.matcher(body)
            if (matcher.find()) {
                Log.d("Otp", matcher.group(0)!!.toString())
                val otp = matcher.group(0)
                //setOtp(matcher.group(0));
                val sotp1 = otp!!.substring(0, 1)
                Log.d("o1", sotp1)
                val sotp2 = otp.substring(1, 2)
                Log.d("o2", sotp2)
                val sotp3 = otp.substring(2, 3)
                Log.d("o3", sotp3)
                val sotp4 = otp.substring(3, 4)
                Log.d("o4", sotp4)
                val sotp5 = otp.substring(4, 5)
                Log.d("o5", sotp5)
                val sotp6 = otp.substring(5)
                Log.d("o6", sotp6)
                otp1!!.setText(sotp1)
                otp2!!.setText(sotp2)
                otp3!!.setText(sotp3)
                otp4!!.setText(sotp4)
                otp5!!.setText(sotp5)
                otp6!!.setText(sotp6)
                Log.d("Complete", "working")
            }
        }
        super.onMessageReceived(message)
    }

    companion object {
        var otp1: EditText? = null
        var otp2: EditText? = null
        var otp3: EditText? = null
        var otp4: EditText? = null
        var otp5: EditText? = null
        var otp6: EditText? = null
        fun setEditTextOtp(
            editText1: EditText?,
            editText2: EditText?,
            editText3: EditText?,
            editText4: EditText?,
            editText5: EditText?,
            editText6: EditText?
        ) {
            otp1 = editText1
            otp2 = editText2
            otp3 = editText3
            otp4 = editText4
            otp5 = editText5
            otp6 = editText6
        }

        @JvmStatic
        fun getToken(context: Context): String? {
            return context.getSharedPreferences("MsgToken", MODE_PRIVATE)
                .getString("fireBaseToken", "empty")
        }
    }
}