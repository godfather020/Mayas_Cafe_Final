package com.example.lottry.data.remote.retrofit.api

import com.example.lottry.data.remote.retrofit.request.*
import com.example.mayasfood.Retrofite.request.*
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Apis {

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.DASHBOARD)
    fun getDashboardItems(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_Branch
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.VERIFY)
    fun getVerifyOtp(
        @Body body: Request_Verify
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.REGISTER)
    fun getRegistraion(
        @Body body: Request_Registration
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.DEVICE_INFO)
    fun sendDeviceDetail(
        @Body body:Request_DeviceInfo
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.USER_PROFILE)
    fun getUserProfile(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.UPDATE_PROFILE)
    fun updateUserProfile(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_updateProfile
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.MY_COUPONS)
    fun myCoupons(
        @Body body: Request_myCoupons
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.POPULAR_FOOD)
    fun getPopularFood(
        @Body body: Request_Branch
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.RESTAURANT_CHOICES)
    fun getRestaurantChoices(
        @Body body: Request_Branch
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.LIST_PRODUCTS)
    fun getFoodCategory(
        @Body body: Request_CategoryDetails
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.ADD_REMOVE_FAV)
    fun addOrRemoveToFav(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_addOrRemoveToFav
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.NOTIFICATION)
    fun getNotificaiton(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.FAVORITE_LIST)
    fun getFavList(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_Branch
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.DEDUCT_WALLET_BALANCE)
    fun deductWalletBalance(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_deductWalletBalance
    ):Call<Response_Common>

    /*@Headers("Content-Type:application/json", "Accept:application/json")
    @FormUrlEncoded
    @POST(Constant.ApiConstant.SET_PROFILE_IMAGE)
    fun setProfileImg(
        @Header (Constant.sharedPrefrencesConstant.X_TOKEN) x_tocken: String,
        @Part image: String
    ):Call<Response_Common>*/

    @Multipart
    @POST(Constants.ApiConstant.SET_PROFILE_IMAGE)
    fun setProfileImg(@Header (Constants.sharedPrefrencesConstant.X_TOKEN) x_tocken:String,
                      @Part  image: MultipartBody.Part
    ): Call<Response_Common>?

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.TODAY_WINNERS)
    fun getTodayWinners(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String
    ):Call<Response_Common>

}