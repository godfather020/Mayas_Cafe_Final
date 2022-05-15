package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.Retrofite.request.Request_updateProfile
import com.example.lottry.data.remote.retrofit.response.UserDetail
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserProfile_ViewModel: ViewModel() {

    lateinit var activity: Fragment
    lateinit var loading : ProgressBar

    val commonResponse = MutableLiveData<Response_Common>()
    val commonResponse1 = MutableLiveData<Response_Common>()
    val commonResponse2 = MutableLiveData<Response_Common>()

    fun set_profileImage(activity:Fragment,imgUrl: File):MutableLiveData<Response_Common>{

        this.activity=activity


        //val reqFile: RequestBody = imgUrl.asRequestBody(".png".toMediaTypeOrNull())
        val reqFile: RequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), imgUrl);

        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image",imgUrl.name,reqFile)
        Log.d("imageFile", imgUrl.toString())
        Log.d("imageFile", reqFile.toString())
        Log.d("imageFile", body.toString())
        Log.d("imageFile", Constants.USER_TOKEN)

        val retrofitInstance: RetrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.setProfileImg(Constants.USER_TOKEN,body)


        retrofitData!!.enqueue(object : Callback<Response_Common> {

            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {
//               TODO("Not yet implemented")
                if(response.isSuccessful) {
                    Log.d("imgUploaded", "ImgUploaded")
//                   binding.progessBar.visibility= View.GONE
                    commonResponse.value=response.body()!!
                    var userDetail= UserDetail()

                    if(response.body()!!.getData()!!.result!!.profilePic!=null) {
                        userDetail.profilePic = response.body()!!.getData()!!.result!!.profilePic
                    }
                    Log.d("imgUpload", userDetail.profilePic)

                }else {
                    val element: JsonElement = Gson().fromJson(response.errorBody()!!.string(), JsonElement::class.java)
                    val jsonObject = element.asJsonObject
                    loading.visibility = View.GONE
                    if(jsonObject.get("code").toString().equals("500")){
//                       if(jsonObject.get("message").asString.equals(activity.resources.getString(R.string.please_registered_your_number_),true)){
                        //activity.showToast(jsonObject.get("message").asString)


                        Toast.makeText(activity.context, jsonObject.get("message").asString, Toast.LENGTH_SHORT).show()
                        Log.d("imgUploaded", jsonObject.get("message").asString)
//                       }
                    }

                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
//               TODO("Not yet implemented")
//               binding.progessBar.visibility= View.GONE
                Toast.makeText(activity.context, t.toString(), Toast.LENGTH_SHORT).show()
                loading.visibility = View.GONE
                Log.d("imgUploaded", t.toString())
            }
        })

        return commonResponse
    }

    fun getUserDetails(activity: Fragment, loading : ProgressBar):MutableLiveData<Response_Common>{

        Log.d("userT", Constants.USER_TOKEN)

        this.activity=activity
        this.loading = loading.findViewById(R.id.loading)

        val retrofitInstance: RetrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getUserProfile(Constants.USER_TOKEN)

        retrofitData.enqueue(object : Callback<Response_Common>{
            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {

                if (response.isSuccessful){

                    commonResponse1.value = response.body()

                }
                else{
                    loading.visibility = View.GONE
                    Log.d("error", response.message())
                }

            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {

                Log.d("userDetails", t.toString())
                loading.visibility = View.GONE

                Toast.makeText(activity.context, t.toString(), Toast.LENGTH_SHORT).show()

            }
        })

        return commonResponse1
    }

    fun updateUserProfile(activity: Fragment, userName: String, userAddress : String, userEmail : String): MutableLiveData<Response_Common> {

        this.activity = activity

        val requestUpdateprofile : Request_updateProfile = Request_updateProfile()

        requestUpdateprofile.userName = userName
        requestUpdateprofile.email = userEmail
        requestUpdateprofile.address = userAddress

        updateProfileAPI(requestUpdateprofile)

        return commonResponse2
    }

    private fun updateProfileAPI(param: Request_updateProfile) {

        val retrofitInstance: RetrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.updateUserProfile(Constants.USER_TOKEN, param)

        retrofitData.enqueue(object : Callback<Response_Common>{
            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {
                if (response.isSuccessful){

                    commonResponse2.value = response.body()
                }
                else{
                    loading.visibility = View.GONE
                    Toast.makeText(activity.requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
                Log.d("error", t.toString())
                loading.visibility = View.GONE
            }
        })

    }
}