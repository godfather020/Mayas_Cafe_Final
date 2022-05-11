package com.example.lottry.data.remote.retrofit.api

import com.example.lottry.data.remote.retrofit.request.*
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.Retrofite.request.Request_Registration
import com.example.mayasfood.Retrofite.request.Request_Verify
import com.example.mayasfood.constants.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Apis {

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.LOGIN)
    fun getOtp(
    @Body body:Request_Login
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
    @POST(Constants.ApiConstant.LOTTERIES)
    fun getLotteryDetail(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body:Request_Lottery
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @GET(Constants.ApiConstant.TICKETS)
    fun getTickeList(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.BUY_TICKETS)
    fun buyTicket(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_buyTicket
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.MY_TICKETS)
    fun myTicket(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        //@Body body: Request_myTicket
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.TOP_WINNERS)
    fun getTopWinners(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_topWinners
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @GET(Constants.ApiConstant.WALLET_BALANCE)
    fun getWalletBalance(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.PAYTM_TOKEN)
    fun getPaytmToken(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_paytmChecksum
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.TRANS_HISTORY)
    fun getTransHistory(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_transHistory
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.NOTIFICATION)
    fun getNotificaiton(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_notification
    ):Call<Response_Common>

    @Headers("Content-Type:application/json", "Accept:application/json")
    @POST(Constants.ApiConstant.ADD_WALLET_BALANCE)
    fun addWalletBalance(
        @Header (Constants.sharedPrefrencesConstant.X_TOKEN)x_tocken:String,
        @Body body: Request_addWalletBalance
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