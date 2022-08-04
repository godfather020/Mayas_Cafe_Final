package com.example.mayasfood.Retrofite.request

import com.google.gson.annotations.SerializedName

class Request_Order_Rating {

    @SerializedName("orderId")
    var orderId:String = ""

    @SerializedName("branchId")
    var branchId:String? = null

    @SerializedName("orderRating")
    var orderRating:String? = null

    @SerializedName("orderComment")
    var orderComment:String? = null
}