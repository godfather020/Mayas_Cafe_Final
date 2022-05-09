package com.example.mayasfood.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mayasfood.R
import com.example.mayasfood.activity.ViewModels.Login_ViewModel
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
import kotlin.system.exitProcess

class Login : AppCompatActivity() {
    private var isBackPressed = false
    private var phoneCheck = false
    private lateinit var phoneNumber: String
    lateinit var phoneNum: EditText
    lateinit var signUp: TextView
    lateinit var signIn: Button
    lateinit var back_img: ImageButton
    lateinit var cc: CountryCodePicker
    lateinit var viewModel: Login_ViewModel
    lateinit var auth : FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var mCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(Login_ViewModel::class.java)

        signIn = findViewById(R.id.sign_in)
        phoneNum = findViewById(R.id.phoneNum)
        back_img = findViewById(R.id.back_img)
        signUp = findViewById(R.id.sign_up)
        cc = findViewById(R.id.cc)
        auth = FirebaseAuth.getInstance()


        cc.setOnCountryChangeListener(OnCountryChangeListener {
            Constants.cc = cc.getSelectedCountryCode()
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

                phoneNumber = phoneNum.text.toString()
                Log.d("phone", phoneNumber)

                getSharedPreferences("UserPhone", MODE_PRIVATE).edit().putString("UserPhone", phoneNumber).apply()

                sendVerificationCode(phoneNumber)

                /*viewModel.get_otp(this, phoneNumber).observe(this, Observer {

                    if (it != null){

                        if (it.getSuccess() == true){

                            Log.d("success", "success")

                            getSharedPreferences("UserPhone", MODE_PRIVATE).edit().putString("UserPhone", phoneNumber).apply()

                            startActivity(Intent(this@Login, OTP::class.java))
                            finish()
                        }
                        else {

                            Log.d("success", "failed")

                            val intent : Intent = Intent(this@Login, Registration::class.java)

                            intent.putExtra("UserPhone", phoneNumber)
                            startActivity(intent)
                            finish()

                        }
                    }

                    else {

                        Log.d("success", "failed1")
                        val intent : Intent = Intent(this@Login, Registration::class.java)

                        intent.putExtra("UserPhone", phoneNumber)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                })*/


            } else {
                Toast.makeText(this@Login, "Check Information", Toast.LENGTH_SHORT).show()
            }
        })
        signUp.setOnClickListener(View.OnClickListener {

            val intent : Intent = Intent(this@Login, Registration::class.java)
            intent.putExtra("UserPhone", "")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
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

    private fun sendVerificationCode(phoneNumber: String) {

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                val intent: Intent = Intent(this@Login, OTP::class.java)
                intent.putExtra("verifyID", verificationId)
                startActivity(intent)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+phoneNumber)      // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    override fun onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this@Login, "Press again to exit", Toast.LENGTH_SHORT).show()
        isBackPressed = true
        Handler().postDelayed({ isBackPressed = false }, 2000)
    }
}