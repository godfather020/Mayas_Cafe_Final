package com.example.mayasfood.activity

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import android.os.Bundle
import com.example.mayasfood.R
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
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
import java.io.*

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
    lateinit var profile_btn: ImageView
    lateinit var profile_img: ImageView

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
        profile_btn = findViewById(R.id.profileBtn_r)
        profile_img = findViewById(R.id.profileImg_r)

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

        profile_btn.setOnClickListener {

            selectImage()
        }

        signUp.setOnClickListener(View.OnClickListener {
            user_name = userName.getText().toString()
            user_phone = Constants.cc + phoneNum.getText().toString()
            Log.d("PhoneNo", user_phone!!)
            dataCheck = Functions.checkData(user_name, user_phone, userName, phoneNum)
            if (dataCheck) {

                user_phone = phoneNum.text.toString()
                Log.d("PhoneNo", user_phone!!)
                viewModel.registerUser(this, user_phone!!, user_name!!).observe(this, androidx.lifecycle.Observer { it ->

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

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = File(Environment.getExternalStorageDirectory().toString(), "temp.jpg")
                intent.putExtra("File", f)

                //context?.let { FileProvider.getUriForFile(it, requireContext().getApplicationContext().getPackageName() + ".provider", f) }
                startActivityForResult(intent, 1)
            } else if (options[item] == "Choose from Gallery") {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }


    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            Log.d("requestCode", requestCode.toString())
            if (requestCode == 1) {
                profile_img.setImageBitmap(data!!.extras!!.get("data") as Bitmap?)
                Log.d("requestCode", requestCode.toString())
                var f = File(Environment.getExternalStorageDirectory().toString())
                //for (temp in f.listFiles()!!) {
                //Log.d("cameraIN", temp.name)
                //if (temp.name == "temp.jpg") {
                //Log.d("cameraIN", temp.name)
                //}
                //  }
                try {
                    val bitmap: Bitmap
                    val bitmapOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(
                        f.absolutePath,
                        bitmapOptions
                    )
                    profile_img.setImageBitmap(bitmap)
                    val path = (Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator
                            + "Phoenix" + File.separator + "default")
                    f.delete()
                    var outFile: OutputStream? = null
                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
                    try {
                        outFile = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile)
                        outFile.flush()
                        outFile.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == 2) {
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor =
                    selectedImage?.let { this.getContentResolver().query(it, filePath, null, null, null) }!!
                c.moveToFirst()
                val columnIndex: Int = c.getColumnIndex(filePath[0])
                val picturePath: String = c.getString(columnIndex)
                c.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                Log.w(
                    "path of image from gallery......******************.........",
                    picturePath + ""
                )
                profile_img.setImageBitmap(thumbnail)
            }
        }
    }
}