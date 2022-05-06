package com.example.mayasfood.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mayasfood.R
import android.text.TextWatcher
import android.text.Editable
import android.content.Intent
import com.example.mayasfood.activity.Login
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.FirebaseCloudMsg
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.mayasfood.activity.ViewModels.Login_ViewModel
import com.example.mayasfood.activity.ViewModels.OTP_ViewModel
import com.example.mayasfood.activity.ViewModels.Registration_ViewModel
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.functions.Functions
import java.util.*
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {
    private var otpData = false
    private lateinit var otp_1: String
    private lateinit var otp_2: String
    private lateinit var otp_3: String
    private lateinit var otp_4: String
    private lateinit var otp1: EditText
    private lateinit var otp2: EditText
    private lateinit var otp3: EditText
    private lateinit var otp4: EditText
    lateinit var timer: TextView
    lateinit var img_back_otp: ImageButton
    lateinit var submit: Button
    lateinit var resend: Button

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
        timer = findViewById(R.id.timer)
        img_back_otp = findViewById(R.id.img_back_otp)
        submit = findViewById(R.id.submit)
        resend = findViewById(R.id.resend)
        Functions.otpTextChange(otp1, otp2, otp1)
        Functions.otpTextChange(otp2, otp3, otp1)
        Functions.otpTextChange(otp3, otp4, otp2)
        Functions.otpTextChange(otp4, otp4, otp3)
        countdownTimer()

        if (getOTP == "1"){

            var userPhone = getSharedPreferences("UserPhone", MODE_PRIVATE).getString("UserPhone", "empty").toString()
            Log.d("userPH", userPhone)

            viewModel.get_otp(this, userPhone)
        }

        otp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (otp4.getText().toString().length > 0) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(otp4.getWindowToken(), 0)
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
            var userPhone = getSharedPreferences("UserPhone", MODE_PRIVATE).getString("UserPhone", "empty").toString()

            Log.d("userPH1", userPhone)

            viewModel.get_otp(this, userPhone)



        })
        submit.setOnClickListener(View.OnClickListener {
            otp_1 = otp1.getText().toString()
            otp_2 = otp2.getText().toString()
            otp_3 = otp3.getText().toString()
            otp_4 = otp4.getText().toString()
            otpData = Functions.checkOtp(otp_1, otp_2, otp_3, otp_4, otp1, otp2, otp3, otp4)
            if (otpData) {

                var otp = otp_1+otp_2+otp_3+otp_4
                var userPhone = getSharedPreferences("UserPhone", MODE_PRIVATE).getString("UserPhone", "empty").toString()

                viewModel.verify_otp(this, userPhone, otp).observe(this, androidx.lifecycle.Observer {


                    if (it != null){

                        if (it.getSuccess()!!){

                            getSharedPreferences("DeviceToken", MODE_PRIVATE).edit().putString("DeviceToken", it.getData()!!.token).apply()

                            Log.d("OTPToken", getSharedPreferences("DeviceToken", MODE_PRIVATE).getString("DeviceToken", "Empty").toString())
                            getSharedPreferences("LogIn", MODE_PRIVATE).edit().putBoolean("LogIn", true).apply()
                            startActivity(Intent(this@OTP, DashBoard::class.java))
                            finish()
                        }

                    }
                })
            } else {
                Toast.makeText(this@OTP, "Check information", Toast.LENGTH_SHORT).show()
            }
        })
        FirebaseCloudMsg().setEditTextOtp(otp1, otp2, otp3, otp4)
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
}