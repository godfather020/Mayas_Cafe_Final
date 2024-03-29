package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_ProductDetails
import com.example.lottry.data.remote.retrofit.request.Request_addOrRemoveToFav
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleItem_viewModel : ViewModel() {

    lateinit var activity: Fragment

    val commonResponse = MutableLiveData<Response_Common>()
    val commonResponse1 = MutableLiveData<Response_Common>()
    val commonResponse2 = MutableLiveData<Response_Common>()

    lateinit var loading : ProgressBar
    lateinit var auth : FirebaseAuth
    lateinit var favImg : ImageView

    fun getItemDetails(activity : Fragment, productId : String, loading: ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity

        this.loading = loading.findViewById(R.id.loading_singleItem)

        auth = FirebaseAuth.getInstance()

        val requestProductdetails : Request_ProductDetails = Request_ProductDetails()

        requestProductdetails.productId = productId

        getAllOrdersAPI(requestProductdetails)

        return commonResponse
    }

    fun addOrRemoveFav(activity : Fragment, productId : String, branchId : String, favImg : ImageView) : MutableLiveData<Response_Common>{

        this.activity = activity
        val request_addOrRemoveToFav = Request_addOrRemoveToFav()
        request_addOrRemoveToFav.branchId = branchId
        request_addOrRemoveToFav.productId = productId
        this.favImg = favImg.findViewById(R.id.singleO_addToFav)
        addOrRemoveFavAPI(request_addOrRemoveToFav)

        return commonResponse1
    }

    fun getProductRatingComments(activity : Fragment, productId : String, page : Int, limit : Int) : MutableLiveData<Response_Common>{

        this.activity = activity

        val requestProductDetails = Request_ProductDetails()
        requestProductDetails.productId = productId
        requestProductDetails.limit = limit
        requestProductDetails.page = page

        getRatingCommentsFromAPI(requestProductDetails)

        return commonResponse2


    }

    private fun getRatingCommentsFromAPI(param: Request_ProductDetails) {

        val retrofitInstance = RetrofitInstance()

        val retrofitData : Call<Response_Common>

        if (auth.currentUser != null || Constants.isLogin != false){

            retrofitData = retrofitInstance.retrofit.getProductRatingComment(Constants.USER_TOKEN, param)
        }else {

            retrofitData = retrofitInstance.retrofit.getProductRatingComment("x-token",param)
        }

        retrofitData.enqueue(object : Callback<Response_Common> {
            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {

                if (response.isSuccessful) {

                    //Toast.makeText(activity.context, response.message().toString(), Toast.LENGTH_SHORT).show()
                    commonResponse2.value = response.body()
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
                Toast.makeText(activity.context, t.toString(), Toast.LENGTH_SHORT).show()
            }


        })

    }

    private fun addOrRemoveFavAPI(param: Request_addOrRemoveToFav) {

        val retrofitInstance = RetrofitInstance()

        //Log.d("click", holder.getBindingAdapterPosition().toString())

        val retrofitData = retrofitInstance.retrofit.addOrRemoveToFav(
            Constants.USER_TOKEN,
            param
        )

        retrofitData.enqueue(object : Callback<Response_Common> {
            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {
                if (response.isSuccessful) {

                    if (response.body()!!.getData()!!.productId != null) {

                        favImg.setImageResource(R.drawable.red_heart)

                    }
                    else{

                        favImg.setImageResource(R.drawable.bi_heart)
                    }
                } else {

                    favImg.setImageResource(R.drawable.bi_heart)
                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
                Toast.makeText(activity.context, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAllOrdersAPI(param: Request_ProductDetails) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData : Call<Response_Common>

        if (auth.currentUser != null || Constants.isLogin != false){

            Log.d("work12312", "working")
            retrofitData = retrofitInstance.retrofit.getProductDetail(Constants.USER_TOKEN, param)
        }else {

            Log.d("work", "working")
            retrofitData = retrofitInstance.retrofit.getProductDetail("x-token",param)
        }

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse.value = response.body()

                    Log.d("Orders", "success")
                    //Toast.makeText(activity.requireContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    loading.visibility = View.GONE
                    //Toast.makeText(activity.requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                    Log.d("Orders", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}