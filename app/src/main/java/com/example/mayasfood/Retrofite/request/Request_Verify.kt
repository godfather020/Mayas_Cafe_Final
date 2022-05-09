package com.example.mayasfood.Retrofite.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Request_Verify {

    @SerializedName("phoneNumber")
    var phoneNumber:String=""

    @SerializedName("deviceToken")
    var deviceToken:String = ""

    @SerializedName("deviceType")
    var deviceType:String = ""

    @SerializedName("deviceId")
    var deviceId : String = ""

    @SerializedName("deviceName")
    var deviceName : String = ""

    @SerializedName("osVersion")
    var osVersion : String = ""
}