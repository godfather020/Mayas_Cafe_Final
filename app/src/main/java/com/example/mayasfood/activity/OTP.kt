package com.example.mayasfood.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.provider.Telephony
import android.telephony.SmsMessage
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.VerifiedInputEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lottry.utils.shared_prefrence.SharedPreferencesUtil
import com.example.mayasfood.FirebaseCloudMsg
import com.example.mayasfood.R
import com.example.mayasfood.activity.ViewModels.OTP_ViewModel
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.functions.Functions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class OTP : AppCompatActivity() {

    private var isBackPressed = false
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
    lateinit var loading : ProgressBar
    //lateinit var mCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth : FirebaseAuth
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        viewModel = ViewModelProvider(this).get(OTP_ViewModel::class.java)

        var token = FirebaseCloudMsg.getToken(this)

        Log.d("tokenOTP", token.toString())

        val getOTP = intent.getStringExtra("getOtp").toString()

        val code = getSharedPreferences("OTP", MODE_PRIVATE).getString("OTP", null)

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
        loading = findViewById(R.id.loading_bar)

        Functions.otpTextChange(otp1, otp2, otp1)
        Functions.otpTextChange(otp2, otp3, otp1)
        Functions.otpTextChange(otp3, otp4, otp2)
        Functions.otpTextChange(otp4, otp5, otp3)
        Functions.otpTextChange(otp5, otp6, otp4)
        Functions.otpTextChange(otp6, otp6, otp5)

        sharedPreferencesUtil = SharedPreferencesUtil(this)

        countdownTimer()

        receiveSms()

        auth = FirebaseAuth.getInstance()

        //val code = intent.getStringExtra("code").toString()

        verificationId = intent.getStringExtra("verifyID").toString()

        Log.d("ID", verificationId)

        userPhone = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_P, "empty")
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
                    this@OTP.submit.callOnClick()
                }
            }
        })

        if (code != null){

            Log.d("OTP", code)

            val sotp1 = code.substring(0, 1)
            Log.d("o1", sotp1)
            val sotp2 = code.substring(1, 2)
            Log.d("o2", sotp2)
            val sotp3 = code.substring(2, 3)
            Log.d("o3", sotp3)
            val sotp4 = code.substring(3, 4)
            Log.d("o4", sotp4)
            val sotp5 = code.substring(4, 5)
            Log.d("o5", sotp5)
            val sotp6 = code.substring(5)
            Log.d("o6", sotp6)

            otp1.setText(sotp1)
            otp2.setText(sotp2)
            otp3.setText(sotp3)
            otp4.setText(sotp4)
            otp5.setText(sotp5)
            otp6.setText(sotp6)
        }

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
            val userPhone =
                getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_P, "empty")
                    .toString()

            Log.d("userPH1", userPhone)

            loading.visibility = View.VISIBLE

            sendVerificationCode(userPhone)

            //viewModel.get_otp(this, userPhone)
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

                loading.visibility = View.VISIBLE
                var otp = otp_1 + otp_2 + otp_3 + otp_4 + otp_5 + otp_6
                var userPhone =
                    getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_P, "empty")
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

        FirebaseCloudMsg.setEditTextOtp(otp1, otp2, otp3, otp4, otp5, otp6)
    }

    private fun verifyCode(code: String) {

            val phoneAuthCredential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationId, code)
            signInByCredentials(phoneAuthCredential)

    }

    private fun signInByCredentials(phoneAuthCredential: PhoneAuthCredential) {
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {

                val deviceName = Build.BRAND + " " + Build.MODEL
                viewModel.verify_otp(this, userPhone, Settings.Secure.ANDROID_ID, deviceName, Build.VERSION.RELEASE).observe(this, androidx.lifecycle.Observer {

                    if (it != null) {

                        if (it.getSuccess()!!) {

                            loading.visibility = View.GONE

                            Toast.makeText(
                                applicationContext,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d(
                                "OTPToken", getSharedPreferences(
                                    "DeviceToken",
                                    MODE_PRIVATE
                                ).getString("DeviceToken", "Empty").toString()
                            )

                            getSharedPreferences("LogIn", MODE_PRIVATE).edit().putBoolean("LogIn", true).apply()

                            getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).edit().putString(Constants.sharedPrefrencesConstant.USER_P, it.getData()!!.result!!.phoneNumber).apply()
                            sharedPreferencesUtil.saveString(Constants.sharedPrefrencesConstant.USER_N , it.getData()!!.result!!.userName)
                            getSharedPreferences(Constants.sharedPrefrencesConstant.USER_N, MODE_PRIVATE).edit().putString(Constants.sharedPrefrencesConstant.USER_N, it.getData()!!.result!!.userName).apply()
                            getSharedPreferences(Constants.sharedPrefrencesConstant.USER_I, MODE_PRIVATE).edit().putString(Constants.sharedPrefrencesConstant.USER_I, it.getData()!!.result!!.profilePic).apply()
                            getSharedPreferences(Constants.sharedPrefrencesConstant.X_TOKEN, MODE_PRIVATE).edit().putString(Constants.sharedPrefrencesConstant.X_TOKEN, it.getData()!!.token).apply()
                            sharedPreferencesUtil.saveString(Constants.sharedPrefrencesConstant.USER_I, it.getData()!!.result!!.profilePic)

                            Log.d("userN",
                                sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_N)
                                    .toString()
                            )

                            Log.d("userToken",
                                getSharedPreferences(Constants.sharedPrefrencesConstant.X_TOKEN, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.X_TOKEN, "")
                                    .toString()
                            )

                            val intent: Intent = Intent(this, DashBoard::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                })

            } else {

                loading.visibility = View.GONE
                Toast.makeText(applicationContext, "OTP not match", Toast.LENGTH_SHORT).show()
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

    private fun sendVerificationCode(phoneNumber: String) {
        loading.visibility = View.GONE
        val mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                loading.visibility = View.GONE

                Toast.makeText(applicationContext, "Enter 6 digit OTP", Toast.LENGTH_SHORT).show()

                val code = credential.smsCode

                if (code != null){

                    Log.d("OTP", code)

                    val sotp1 = code.substring(0, 1)
                    Log.d("o1", sotp1)
                    val sotp2 = code.substring(1, 2)
                    Log.d("o2", sotp2)
                    val sotp3 = code.substring(2, 3)
                    Log.d("o3", sotp3)
                    val sotp4 = code.substring(3, 4)
                    Log.d("o4", sotp4)
                    val sotp5 = code.substring(4, 5)
                    Log.d("o5", sotp5)
                    val sotp6 = code.substring(5)
                    Log.d("o6", sotp6)

                    otp1.setText(sotp1)
                    otp2.setText(sotp2)
                    otp3.setText(sotp3)
                    otp4.setText(sotp4)
                    otp5.setText(sotp5)
                    otp6.setText(sotp6)

                }

            }

            override fun onVerificationFailed(e: FirebaseException) {

                loading.visibility = View.GONE

                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("user", "codeSent")
                loading.visibility = View.GONE

                /*val intent: Intent = Intent(this@Login, OTP::class.java)
                intent.putExtra("verifyID", verificationId)
                startActivity(intent)*/
            }
        }

        Log.d("userPh", phoneNumber)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)      // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    override fun onBackPressed() {

        loading.visibility = View.GONE

        if (isBackPressed) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this@OTP, "Press again to exit", Toast.LENGTH_SHORT).show()
        isBackPressed = true
        Handler().postDelayed({ isBackPressed = false }, 2000)
    }

    private fun receiveSms() {
        val br = object : BroadcastReceiver(){
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onReceive(context: Context?, intent: Intent?) {

                resend.visibility = View.GONE
                timer.visibility = View.GONE

                for (sms : SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                    //Toast.makeText(applicationContext,sms.displayMessageBody, Toast.LENGTH_LONG).show()
                    val smsBody = sms.messageBody
                    Log.d("msgBody", smsBody)
                    getOtpFromMessage(smsBody)
                    //val getOtp = smsBody.split("Your OTP: ").toTypedArray()[1]
                    //Log.d("otp", getOtp)
                    //setOtp(getOtp)
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getOtpFromMessage(message: String) {
        // This will match any 4 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{6}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            Log.d("Otp", matcher.group(0))
            //setOtp(otp = matcher.group(0))
            val code =  matcher.group(0)

            val sotp1 = code!!.substring(0, 1)
            Log.d("o1", sotp1)
            val sotp2 = code.substring(1, 2)
            Log.d("o2", sotp2)
            val sotp3 = code.substring(2, 3)
            Log.d("o3", sotp3)
            val sotp4 = code.substring(3, 4)
            Log.d("o4", sotp4)
            val sotp5 = code.substring(4, 5)
            Log.d("o5", sotp5)
            val sotp6 = code.substring(5)
            Log.d("o6", sotp6)

            otp1.setText(sotp1)
            otp2.setText(sotp2)
            otp3.setText(sotp3)
            otp4.setText(sotp4)
            otp5.setText(sotp5)
            otp6.setText(sotp6)

            this.submit.callOnClick()
        }
    }

}


