package com.example.mayasfood.activity

import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import android.os.Bundle
import com.example.mayasfood.R
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mayasfood.activity.ViewModels.Login_ViewModel
import com.example.mayasfood.activity.ViewModels.OTP_ViewModel
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import com.example.mayasfood.activity.ViewModels.Registration_ViewModel
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.functions.Functions

class Registration : AppCompatActivity() {

    private var isBackPressed = false
    lateinit var userName: EditText
    lateinit var phoneNum: EditText
    lateinit var signUp: Button
    lateinit var skip: TextView
    lateinit var back_img_r: ImageButton
    private var dataCheck = false
    private var user_name: String? = null
    private var user_phone: String? = null
    lateinit var cc_r: CountryCodePicker

    lateinit var viewModel: Registration_ViewModel
    //lateinit var viewModel1: Login_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        viewModel = ViewModelProvider(this).get(Registration_ViewModel::class.java)
        //viewModel1 = ViewModelProvider(this).get(Login_ViewModel::class.java)

        userName = findViewById(R.id.userName_r)
        phoneNum = findViewById(R.id.phoneNum_r)
        signUp = findViewById(R.id.signUp)
        skip = findViewById(R.id.skip)
        back_img_r = findViewById(R.id.back_img_r)
        cc_r = findViewById(R.id.cc_r)

        if (!getIntent().getStringExtra("UserPhone").isNullOrEmpty()) {

            var user_phone_intent = getIntent().getStringExtra("UserPhone").toString()

            phoneNum.setText(user_phone_intent)
            Log.d("user_phone_intent", user_phone_intent)
        }

        back_img_r.setOnClickListener {
            startActivity(Intent(this@Registration, Login::class.java))
            finish()
        }

        cc_r.setOnCountryChangeListener(OnCountryChangeListener {
            Constants.cc = cc_r.getSelectedCountryCode()
            Log.d("CountryCode", Constants.cc)
        })

        signUp.setOnClickListener(View.OnClickListener {
            user_name = userName.getText().toString()
            user_phone = Constants.cc + phoneNum.getText().toString()
            Log.d("PhoneNo", user_phone!!)
            dataCheck = Functions.checkData(user_name, user_phone, userName, phoneNum)
            if (dataCheck) {

                user_phone = phoneNum.text.toString()
                Log.d("PhoneNo", user_phone!!)
                viewModel.registerUser(this, user_name!!, user_phone!!).observe(this, androidx.lifecycle.Observer { it ->

                    if(it!=null){

                        if(it.getSuccess()!!) {

                            Log.d("PhoneNo", user_phone!!)

                            getSharedPreferences("UserPhone", MODE_PRIVATE).edit().putString("UserPhone", user_phone!!).apply()

                            val intent: Intent = Intent(this@Registration, OTP::class.java)
                            intent.putExtra("getOtp", "1")
                            startActivity(intent)
                            finish()

                        }
                    }
                })
            } else {
                Toast.makeText(this@Registration, "Check Information", Toast.LENGTH_SHORT).show()
            }
        })
        skip.setOnClickListener(View.OnClickListener {

            getSharedPreferences("LogIn", MODE_PRIVATE).edit().putBoolean("LogIn", false).apply()
            startActivity(Intent(this@Registration, DashBoard::class.java))
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

    override fun onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this@Registration, "Press again to exit", Toast.LENGTH_SHORT).show()
        isBackPressed = true
        Handler().postDelayed({ isBackPressed = false }, 2000)
    }


}