package com.example.lottry.data.remote.retrofit.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class User {

    @SerializedName("id"           )
    var id           : Int?     = null

    @SerializedName("userName"     )
    var userName     : String?  = null

    @SerializedName("email"        )
    var email        : String?  = null

    @SerializedName("phoneNumber"  )
    var phoneNumber  : String?  = null

    @SerializedName("profilePic"   )
    var profilePic   : String?  = null

    @SerializedName("deviceId"     )
    var deviceId     : String?  = null

    @SerializedName("deviceName"   )
    var deviceName   : String?  = null

    @SerializedName("osVersion"    )
    var osVersion    : String?  = null

    @SerializedName("deviceType"   )
    var deviceType   : String?  = null

    @SerializedName("deviceToken"  )
    var deviceToken  : String?  = null

    @SerializedName("otp"          )
    var otp          : String?  = null

    @SerializedName("otpExpire"    )
    var otpExpire    : String?  = null

    @SerializedName("isVerified"   )
    var isVerified   : Boolean? = null

    @SerializedName("status"       )
    var status       : Boolean? = null

    @SerializedName("couponStatus" )
    var couponStatus : Boolean? = null

    @SerializedName("createdAt"    )
    var createdAt    : String?  = null

    @SerializedName("updatedAt"    )
    var updatedAt    : String?  = null

    @SerializedName("address")
    var address : String? = null

}
