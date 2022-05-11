package com.example.mayasfood.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lottry.utils.shared_prefrence.SharedPreferencesUtil
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.UserProfile_ViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class UserProfile_frag : Fragment() {

    lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    lateinit var userPro: CircleImageView
    lateinit var userProfile_edit: ImageButton
    lateinit var viewModel : UserProfile_ViewModel
    lateinit var dashBoard: DashBoard
    private var mImageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_user_profile_frag, container, false)

        dashBoard = activity as DashBoard

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        sharedPreferencesUtil = SharedPreferencesUtil(requireActivity().applicationContext)

        viewModel = ViewModelProvider(this).get(UserProfile_ViewModel::class.java)

        userProfile_edit = view.findViewById(R.id.userProfile_edit)

        userProfile_edit.setOnClickListener {

            showProfileEditPopup()
        }

        return view
    }

    private fun showProfileEditPopup(){

        var dialog = Dialog(requireContext());
        dialog.setCancelable(false);

        var view = this.layoutInflater.inflate(R.layout.profile_edit_popup, null)

        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
            dialog.getWindow()!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        userPro = view.findViewById<CircleImageView>(R.id.circleImageView)
        val close = view.findViewById<ImageButton>(R.id.userEdit_close)

        userPro.setOnClickListener {

            selectImage()
        }

        close.setOnClickListener {

            dialog.cancel()
        }

        dialog.show();
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {

                try {
                    val photoFile = createImageFile()

                //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = Uri.fromFile(createImageFile())
                    if (photoFile != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(intent, 1);
                    }

                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("excep", "IOException")
                }
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
        if (resultCode == AppCompatActivity.RESULT_OK) {

            Log.d("requestCode", requestCode.toString())
            if (requestCode == 1) {

                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        Uri.parse(mCurrentPhotoPath)
                    )
                    userPro.setImageBitmap(mImageBitmap)

                    Log.d("path", mCurrentPhotoPath.toString())
                    Log.d("path", mImageBitmap.toString())
                    sendProfileImg(mCurrentPhotoPath.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                //userPro.setImageBitmap(data!!.extras!!.get("data") as Bitmap?)
                /*Log.d("requestCode", requestCode.toString())
                val f = File(Environment.getExternalStorageDirectory().toString())
               // Log.d("cameraINpath", f.absolutePath)
                for (temp in f.listFiles()!!) {
                //Log.d("cameraIN", temp.name)
                if (temp.name == ) {
               // Log.d("cameraIN1", temp.name)
                }
                  }
                try {
                    val bitmap: Bitmap
                    val bitmapOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(
                        f.absolutePath,
                        bitmapOptions
                    )
                    userPro.setImageBitmap(bitmap)
                    val path = (Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator
                            + "Phoenix" + File.separator + "default")
                    f.delete()
                    var outFile: OutputStream? = null
                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
                    Log.d("cameraPath", path)
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
                }*/


            } else if (requestCode == 2) {
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor =
                    selectedImage?.let { requireContext().contentResolver.query(it, filePath, null, null, null) }!!
                c.moveToFirst()
                val columnIndex: Int = c.getColumnIndex(filePath[0])
                val picturePath: String = c.getString(columnIndex)
                c.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                Log.w(
                    "path of image from gallery......******************.........",
                    picturePath + ""
                )
                userPro.setImageBitmap(thumbnail)
                sendProfileImg(picturePath)
            }
        }
    }

    private fun sendProfileImg(picturePath: String) {

        val file: File = File(picturePath)

        viewModel.set_profileImage(this, file).observe(this, androidx.lifecycle.Observer {

            if (it!=null){

                if(it.getSuccess()!!){

                    Toast.makeText(activity, "Image Uploaded", Toast.LENGTH_SHORT).show()

                    val userPic = it.getData()!!.result!!.profilePic

                    sharedPreferencesUtil.saveString(Constants.sharedPrefrencesConstant.USER_I, it.getData()!!.result!!.profilePic)

                    Picasso.get()
                        .load(Constants.UserProfile_Path + userPic)
                        .into(dashBoard.user_profile)

                    Log.d("profileImg", it.getData()!!.result!!.profilePic)
                }

            }

            else {

                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",  // suffix
            storageDir // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.absolutePath
        Log.d("path", mCurrentPhotoPath.toString())
        return image
    }
}