package com.example.mayasfood.Retrofite.response

import com.google.gson.annotations.SerializedName

class Customer {

    @SerializedName("userName"   )
    var userName   : String? = null

    @SerializedName("profilePic" )
    var profilePic : String? = null
}