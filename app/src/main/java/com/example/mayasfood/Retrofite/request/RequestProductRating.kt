package com.example.mayasfood.Retrofite.request

import com.google.gson.annotations.SerializedName

class RequestProductRating {

    @SerializedName("productId")
    var productId:String = ""

    @SerializedName("ratingScore")
    var ratingScore:String? = null

    @SerializedName("ratingcomment")
    var ratingcomment:String? = null

    @SerializedName("branchId")
    var branchId:String? = null
}