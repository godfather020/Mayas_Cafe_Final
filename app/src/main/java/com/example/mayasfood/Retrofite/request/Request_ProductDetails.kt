package com.example.lottry.data.remote.retrofit.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Request_ProductDetails {

    @SerializedName("productId")
    var productId:String = ""

    @SerializedName("page")
    var page:Int? = null

    @SerializedName("limit")
    var limit:Int? = null
}