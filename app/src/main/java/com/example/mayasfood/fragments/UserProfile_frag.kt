package com.example.mayasfood.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lottry.utils.shared_prefrence.SharedPreferencesUtil
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.UserProfile_ViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.IOException
import java.io.OutputStream


class UserProfile_frag : Fragment() {

    lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    lateinit var userPro: CircleImageView
    lateinit var userProfile_edit: ImageButton
    lateinit var viewModel : UserProfile_ViewModel
    lateinit var dashBoard: DashBoard
    private var mImageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null
    var photoFile: File? = null
    lateinit var picture_icon: ImageView
    var uploadImgPath: String? = null
    lateinit var userImage : CircleImageView
    lateinit var userName : EditText
    lateinit var userAddress : EditText
    lateinit var userPhone : TextView
    lateinit var userEmail : EditText
    lateinit var updateProfile : Button
    lateinit var changePro: ImageButton
    lateinit var cancelUpdate : Button
    lateinit var loading : ProgressBar
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_user_profile_frag, container, false)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Profile")
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        sharedPreferencesUtil = SharedPreferencesUtil(requireActivity().applicationContext)

        viewModel = ViewModelProvider(this).get(UserProfile_ViewModel::class.java)

        userImage = view.findViewById(R.id.user_img)
        userName = view.findViewById(R.id.userName)
        userEmail = view.findViewById(R.id.email)
        userPhone = view.findViewById(R.id.phone)
        userAddress = view.findViewById(R.id.address)
        updateProfile = view.findViewById(R.id.update_profile)
        userProfile_edit = view.findViewById(R.id.userProfile_edit)
        changePro = view.findViewById(R.id.change_pic)
        cancelUpdate = view.findViewById(R.id.cancelUpdate)
        loading = view.findViewById(R.id.loading)

        loading.visibility = View.VISIBLE

        getUserDetals()

        userProfile_edit.setOnClickListener {

            userName.isEnabled = true
            userEmail.isEnabled = true
            userAddress.isEnabled = true
            updateProfile.visibility = View.VISIBLE
            cancelUpdate.visibility = View.VISIBLE
            userProfile_edit.visibility = View.GONE

            //showProfileEditPopup()
        }

        changePro.setOnClickListener {

            selectImage()
        }

        updateProfile.setOnClickListener {

            loading.visibility = View.VISIBLE

            if (uploadImgPath != null){

                sendProfileImg(uploadImgPath!!)
            }

            updateUserDetails(userName.text.toString(), userAddress.text.toString(), userEmail.text.toString())

            updateProfile.visibility = View.GONE
            cancelUpdate.visibility = View.GONE
            userProfile_edit.visibility = View.VISIBLE
            userEmail.isEnabled = false
            userName.isEnabled = false
            userAddress.isEnabled = false
        }

        cancelUpdate.setOnClickListener {

            userEmail.isEnabled = false
            userName.isEnabled = false
            userAddress.isEnabled = false
            updateProfile.visibility = View.GONE
            cancelUpdate.visibility = View.GONE
            userProfile_edit.visibility = View.VISIBLE
        }


        return view
    }

    private fun updateUserDetails(user_Name: String, user_address: String, userEmail: String) {

        viewModel.updateUserProfile(this, user_Name, user_address, userEmail).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    loading.visibility = View.GONE

                    Constants.USER_NAME = user_Name

                    sharedPreferencesUtil.saveString(Constants.sharedPrefrencesConstant.USER_N, user_Name)

                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()

                }
                else{

                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

            }
            else{

                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getUserDetals() {

        viewModel.getUserDetails(this, loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    val userProfile =
                        sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_I)

                    Log.d("userP", it.getData()!!.user!!.profilePic.toString())
                    Log.d("userN", it.getData()!!.user!!.userName.toString())
                    Log.d("userE", it.getData()!!.user!!.email.toString())
                    Log.d("userA", it.getData()!!.user!!.address.toString())
                    Log.d("userP", it.getData()!!.user!!.phoneNumber.toString())

                    Picasso.get()
                        .load(Constants.UserProfile_Path + it.getData()!!.user!!.profilePic.toString())
                        .into(userImage)

                    userName.setText(it.getData()!!.user!!.userName.toString())
                    if(it.getData()!!.user!!.email != null) {
                        userEmail.setText(it.getData()!!.user!!.email.toString())
                    }
                    if (it.getData()!!.user!!.address != null) {
                        userAddress.setText(it.getData()!!.user!!.address.toString())
                    }
                    userPhone.setText(it.getData()!!.user!!.phoneNumber.toString())

                    loading.visibility = View.GONE
                }
            }

        })
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

        val close = view.findViewById<ImageButton>(R.id.close_dialog)
        val update = view.findViewById<Button>(R.id.userEdit_update)

        val userNameE = view.findViewById<EditText>(R.id.userEdit_name)


        userNameE.setText(userName.text.toString())

        userNameE.setText(userName.text.toString())

        userPro.setOnClickListener {
            picture_icon.visibility = View.GONE
            //selectImage()
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

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                photoFile = getPhotoFileUri("temp.jpg")

                val fileProvider = FileProvider.getUriForFile(
                    requireContext(),
                    "com.codepath.fileprovider",
                    photoFile!!
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                if (intent.resolveActivity(requireActivity().packageManager) != null) {

                    startActivityForResult(intent, 1)
                }
            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {

                picture_icon.visibility  = View.VISIBLE

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

                val bitmap: Bitmap? = rotateImageIfRequired(photoFile!!.absolutePath)
                Log.d("camerPath", photoFile!!.absolutePath)

                /*Picasso.get()
                    .load(File(photoFile!!.absoluteFile.toString()))
                    .centerCrop()
                    .into(userPro)*/
                loadBitmapByPicasso(requireContext(), bitmap!!, userImage)
                //userPro.setImageBitmap(bitmap)
                sendProfileImg(photoFile!!.absolutePath)
                uploadImgPath = photoFile!!.absolutePath

            } else if (requestCode == 2) {
                verifyStoragePermissions(requireActivity())
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor =
                    selectedImage?.let {
                        requireContext().contentResolver.query(
                            it,
                            filePath,
                            null,
                            null,
                            null
                        )
                    }!!
                c.moveToFirst()
                val columnIndex: Int = c.getColumnIndex(filePath[0])
                val picturePath: String = c.getString(columnIndex)
                c.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                Log.w(
                    "path of image from gallery......******************.........",
                    picturePath + ""
                )
                userImage.setImageBitmap(thumbnail)
                sendProfileImg(picturePath)
                uploadImgPath = picturePath
            }
        }
    }

    private fun sendProfileImg(picturePath: String) {

        loading.visibility = View.VISIBLE

        val file: File = File(picturePath)

        viewModel.set_profileImage(this, file).observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (it!=null){

                if(it.getSuccess()!!){

                    loading.visibility = View.GONE
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
                loading.visibility = View.GONE
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getPhotoFileUri(fileName: String): File? {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CustomImg")

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("CustomImg", "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator.toString() + fileName)
    }

    fun rotateImageIfRequired(imagePath: String): Bitmap? {
        var degrees = 0
        try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degrees = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degrees = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degrees = 270
            }
        } catch (e: IOException) {
            Log.e("ImageError", "Error in reading Exif data of $imagePath", e)
        }
        val decodeBounds = BitmapFactory.Options()
        decodeBounds.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds)
        val numPixels = decodeBounds.outWidth * decodeBounds.outHeight
        val maxPixels = 2048 * 1536 // requires 12 MB heap
        val options = BitmapFactory.Options()
        options.inSampleSize = if (numPixels > maxPixels) 2 else 1
        bitmap = BitmapFactory.decodeFile(imagePath, options)
        if (bitmap == null) {
            return null
        }
        val matrix = Matrix()
        matrix.setRotate(degrees.toFloat())
        bitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
        return bitmap
    }

    private fun loadBitmapByPicasso(pContext: Context, pBitmap: Bitmap, pImageView: CircleImageView) {
        try {
            val uri = Uri.fromFile(File.createTempFile("temp_file_name", ".jpg", pContext.cacheDir))
            val outputStream: OutputStream? = pContext.contentResolver.openOutputStream(uri)
            pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream!!.close()
            Picasso.get().load(uri).into(pImageView)
        } catch (e: java.lang.Exception) {
            Log.e("LoadBitmapByPicasso", e.message!!)
        }
    }

    public fun verifyStoragePermissions(activity : Activity) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}