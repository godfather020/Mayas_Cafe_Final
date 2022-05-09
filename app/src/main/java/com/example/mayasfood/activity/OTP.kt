package com.example.mayasfood.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mayasfood.FirebaseCloudMsg
import com.example.mayasfood.R
import com.example.mayasfood.activity.ViewModels.OTP_ViewModel
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.functions.Functions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import java.util.*
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {
    private var otpData = false
    private lateinit var otp_1: String
    private lateinit var otp_2: String
    private lateinit var otp_3: String
    private lateinit var otp_4: String
    private lateinit var otp_5: String
    private lateinit var otp_6: String
    private lateinit var otp1: EditText
    private lateinit var otp2: EditText
    private lateinit var otp3: EditText
    private lateinit var otp4: EditText
    private lateinit var otp5: EditText
    private lateinit var otp6: EditText
    lateinit var timer: TextView
    lateinit var img_back_otp: ImageButton
    lateinit var submit: Button
    lateinit var resend: Button
    lateinit var verificationId: String
    lateinit var userPhone : String
    lateinit var viewModel : OTP_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        viewModel = ViewModelProvider(this).get(OTP_ViewModel::class.java)

        val getOTP = intent.getStringExtra("getOtp").toString()

        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)
        otp5 = findViewById(R.id.otp5)
        otp6 = findViewById(R.id.otp6)
        timer = findViewById(R.id.timer)
        img_back_otp = findViewById(R.id.img_back_otp)
        submit = findViewById(R.id.submit)
        resend = findViewById(R.id.resend)
        Functions.otpTextChange(otp1, otp2, otp1)
        Functions.otpTextChange(otp2, otp3, otp1)
        Functions.otpTextChange(otp3, otp4, otp2)
        Functions.otpTextChange(otp4, otp5, otp3)
        Functions.otpTextChange(otp5, otp6, otp4)
        Functions.otpTextChange(otp6, otp6, otp5)
        countdownTimer()

        //val code = intent.getStringExtra("code").toString()

        verificationId = intent.getStringExtra("verifyID").toString()

        Log.d("ID", verificationId)

        userPhone = getSharedPreferences("UserPhone", MODE_PRIVATE).getString("UserPhone", "empty")
            .toString()

        if (getOTP == "1") {


            Log.d("userPH", userPhone)

           // viewModel.get_otp(this, userPhone)
        }

        otp6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (otp6.getText().toString().length > 0) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(otp6.getWindowToken(), 0)
                }
            }
        })
        img_back_otp.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@OTP,
                    Login::class.java
                )
            )
        })
        resend.setOnClickListener(View.OnClickListener {
            resend.setVisibility(View.GONE)
            timer.setVisibility(View.VISIBLE)
            countdownTimer()
            var userPhone =
                getSharedPreferences("UserPhone", MODE_PRIVATE).getString("UserPhone", "empty")
                    .toString()

            Log.d("userPH1", userPhone)

            viewModel.get_otp(this, userPhone)


        })
        submit.setOnClickListener(View.OnClickListener {
            otp_1 = otp1.getText().toString()
            otp_2 = otp2.getText().toString()
            otp_3 = otp3.getText().toString()
            otp_4 = otp4.getText().toString()
            otp_5 = otp5.getText().toString()
            otp_6 = otp6.getText().toString()

            Log.d("Otp", otp_5)

            otpData = Functions.checkOtp(otp_1, otp_2, otp_3, otp_4, otp_5, otp_6, otp1, otp2, otp3, otp4, otp5, otp6)
            if (otpData) {

                var otp = otp_1 + otp_2 + otp_3 + otp_4 + otp_5 + otp_6
                var userPhone =
                    getSharedPreferences("UserPhone", MODE_PRIVATE).getString("UserPhone", "empty")
                        .toString()

                Log.d("OTP", otp)
                var deviceInfo = getSystemDetail()
                Log.d("deviceInfo", deviceInfo)
                verifyCode(otp)

                /*viewModel.verify_otp(this, userPhone, otp)
                    .observe(this, androidx.lifecycle.Observer {


                        if (it != null) {

                            if (it.getSuccess()!!) {

                                getSharedPreferences("DeviceToken", MODE_PRIVATE).edit()
                                    .putString("DeviceToken", it.getData()!!.token).apply()


                            }

                        }
                    })*/
            } else {
                Toast.makeText(this@OTP, "Check information", Toast.LENGTH_SHORT).show()
            }
        })


        FirebaseCloudMsg().setEditTextOtp(otp1, otp2, otp3, otp4, otp5, otp6)


    }

    private fun verifyCode(code: String) {

        val phoneAuthCredential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code)
        signInByCredentials(phoneAuthCredential)
    }

    private fun signInByCredentials(phoneAuthCredential: PhoneAuthCredential) {
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {

                val deviceName = Build.BRAND + " " + Build.MODEL
                viewModel.verify_otp(this, userPhone, Settings.Secure.ANDROID_ID, deviceName, Build.VERSION.RELEASE).observe(this, androidx.lifecycle.Observer {

                    if (it.getSuccess() == true){


                    }
                })

                //Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                Log.d(
                    "OTPToken",
                    getSharedPreferences(
                        "DeviceToken",
                        MODE_PRIVATE
                    ).getString("DeviceToken", "Empty").toString()
                )
                getSharedPreferences("LogIn", MODE_PRIVATE).edit()
                    .putBoolean("LogIn", true).apply()
                startActivity(Intent(this@OTP, DashBoard::class.java))
                finish()
            } else {

                Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun countdownTimer() {

        //60 Second CountDown
        object : CountDownTimer((Constants.duration * 1000).toLong(), 1000) {
            override fun onTick(l: Long) {
                runOnUiThread {
                    val sDuration = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                    )
                    timer!!.text = getString(R.string.OTP_resend) + " " + sDuration + " sec"
                }
            }

            override fun onFinish() {
                resend!!.visibility = View.VISIBLE
                timer!!.visibility = View.GONE
            }
        }.start()
    }

    @SuppressLint("HardwareIds")
    private fun getSystemDetail(): String {
        return "Brand: ${Build.BRAND} \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "Model: ${Build.MODEL} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"
    }

}


