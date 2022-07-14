package com.example.mayasfood.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lottry.utils.shared_prefrence.SharedPreferencesUtil
import com.example.mayasfood.R
import com.example.mayasfood.activity.ViewModels.Login_ViewModel
import com.example.mayasfood.activity.ViewModels.OTP_ViewModel
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.functions.Functions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import java.util.concurrent.TimeUnit

class Login : AppCompatActivity() {
    private var isBackPressed = false
    private var phoneCheck = false
    private lateinit var phoneNumber: String
    lateinit var phoneNum: EditText
    lateinit var signIn: Button
    lateinit var back_img: ImageButton
    lateinit var cc: CountryCodePicker
    lateinit var viewModel: Login_ViewModel
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var loading: ProgressBar
    lateinit var skip: TextView
    var code: String? = null
    lateinit var otpViewmodel: OTP_ViewModel
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(Login_ViewModel::class.java)
        otpViewmodel = ViewModelProvider(this).get(OTP_ViewModel::class.java)

        signIn = findViewById(R.id.sign_in)
        phoneNum = findViewById(R.id.phoneNum)
        back_img = findViewById(R.id.back_img)
        cc = findViewById(R.id.cc)
        auth = FirebaseAuth.getInstance()
        loading = findViewById(R.id.loading_bar)
        skip = findViewById(R.id.skip)

        Constants.cc = "+" + cc.selectedCountryCode

        Log.d("cc", Constants.cc)

        sharedPreferencesUtil = SharedPreferencesUtil(this)

        if (getSharedPreferences("GetStartFirst", MODE_PRIVATE).getBoolean("FirstTime", false)) {

            back_img.visibility = View.GONE
        } else {

            back_img.visibility = View.VISIBLE
        }

        skip.setOnClickListener {

            getSharedPreferences("LogIn", MODE_PRIVATE).edit().putBoolean("LogIn", false).apply()
            sharedPreferencesUtil.saveString(Constants.sharedPrefrencesConstant.USER_N, "Stranger")

            startActivity(Intent(this@Login, DashBoard::class.java))
            finish()
        }

        cc.setOnCountryChangeListener(OnCountryChangeListener {
            Constants.cc = "+" + cc.selectedCountryCode
            Log.d("cc", Constants.cc)
        })
        back_img.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Login, GetStart::class.java))
            finish()
        })
        signIn.setOnClickListener(View.OnClickListener {
            phoneNumber = Constants.cc + phoneNum.getText().toString()
            Log.d("phone", phoneNumber)
            phoneCheck = Functions.checkData(phoneNumber, phoneNum)
            if (phoneCheck) {

                if (phoneNumber.equals("+917000107876")) {

                    getSharedPreferences(
                        Constants.sharedPrefrencesConstant.USER_P,
                        MODE_PRIVATE
                    ).edit().putString(Constants.sharedPrefrencesConstant.USER_P, phoneNumber)
                        .apply()

                    loading.visibility = View.VISIBLE

                    signInUser(phoneNumber)

                } else {

                    Log.d("phone", phoneNumber)

                    getSharedPreferences(
                        Constants.sharedPrefrencesConstant.USER_P,
                        MODE_PRIVATE
                    ).edit().putString(Constants.sharedPrefrencesConstant.USER_P, phoneNumber)
                        .apply()
                    loading.visibility = View.VISIBLE

                    sendVerificationCode(phoneNumber)
                }

            } else {
                Toast.makeText(this@Login, "Check Information", Toast.LENGTH_SHORT).show()
            }
        })

        phoneNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (phoneNum.getText().toString().length == 10) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(phoneNum.getWindowToken(), 0)
                }
            }
        })
    }

    private fun signInUser(userPhone: String) {

        val deviceName = Build.BRAND + " " + Build.MODEL

        otpViewmodel.verify_otp(
            this,
            userPhone,
            Settings.Secure.ANDROID_ID,
            deviceName,
            Build.VERSION.RELEASE
        ).observe(this, androidx.lifecycle.Observer {

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

                    getSharedPreferences("LogIn", MODE_PRIVATE).edit().putBoolean("LogIn", true)
                        .apply()

                    Constants.isLogin = true

                    getSharedPreferences(
                        Constants.sharedPrefrencesConstant.USER_P,
                        MODE_PRIVATE
                    ).edit().putString(
                        Constants.sharedPrefrencesConstant.USER_P,
                        it.getData()!!.result!!.phoneNumber
                    ).apply()
                    sharedPreferencesUtil.saveString(
                        Constants.sharedPrefrencesConstant.USER_N,
                        it.getData()!!.result!!.userName
                    )
                    getSharedPreferences(
                        Constants.sharedPrefrencesConstant.USER_N,
                        MODE_PRIVATE
                    ).edit().putString(
                        Constants.sharedPrefrencesConstant.USER_N,
                        it.getData()!!.result!!.userName
                    ).apply()
                    getSharedPreferences(
                        Constants.sharedPrefrencesConstant.USER_I,
                        MODE_PRIVATE
                    ).edit().putString(
                        Constants.sharedPrefrencesConstant.USER_I,
                        it.getData()!!.result!!.profilePic
                    ).apply()
                    getSharedPreferences(
                        Constants.sharedPrefrencesConstant.X_TOKEN,
                        MODE_PRIVATE
                    ).edit()
                        .putString(Constants.sharedPrefrencesConstant.X_TOKEN, it.getData()!!.token)
                        .apply()
                    sharedPreferencesUtil.saveString(
                        Constants.sharedPrefrencesConstant.USER_I,
                        it.getData()!!.result!!.profilePic
                    )

                    Log.d(
                        "userN",
                        sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_N)
                            .toString()
                    )

                    Log.d(
                        "userToken",
                        getSharedPreferences(
                            Constants.sharedPrefrencesConstant.X_TOKEN,
                            MODE_PRIVATE
                        ).getString(Constants.sharedPrefrencesConstant.X_TOKEN, "")
                            .toString()
                    )

                    val intent: Intent = Intent(this, DashBoard::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    private fun sendVerificationCode(phoneNumber: String) {

        val mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                loading.visibility = View.GONE

                Toast.makeText(applicationContext, "Enter 6 digit OTP", Toast.LENGTH_SHORT).show()

                code = credential.smsCode

                if (null == credential.smsCode) {
                    //loginUser()

                    Log.d("OTP0", "null")
                } else {
                    val code = credential.smsCode
                    if (null != code) {
                        Log.d("OTP1", code.toString())
                        getSharedPreferences("OTP", MODE_PRIVATE).edit().putString("OTP", code)
                            .apply()
                        //otp_view.otp = code
//                        hideKeyboard(etPhoneNumber)
//                        verificationCode = otp_view.otp
//                        setSelected(verify)
//                        authenticateOtp()
                    } else {
                        //loginUser()
                        Log.d("OTP2", "null")
                    }
                }

            }

            override fun onVerificationFailed(e: FirebaseException) {

                loading.visibility = View.GONE

                //Toast.makeText(applicationContext, "Number has been blocked. Please try after sometime.", Toast.LENGTH_LONG).show()
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                storedVerificationId = verificationId

                Log.d("OTP", code.toString())

                loading.visibility = View.GONE
                val intent: Intent = Intent(this@Login, OTP::class.java)
                intent.putExtra("verifyID", storedVerificationId)
                if (code != null) {

                    getSharedPreferences("OTP", MODE_PRIVATE).edit().putString("OTP", code).apply()
                }
                startActivity(intent)
                finish()
            }
        }

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
        Toast.makeText(this@Login, "Press again to exit", Toast.LENGTH_SHORT).show()
        isBackPressed = true
        Handler().postDelayed({ isBackPressed = false }, 2000)
    }
}