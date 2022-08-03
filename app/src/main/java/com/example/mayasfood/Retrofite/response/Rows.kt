package com.example.mayasfood.Retrofite.response

import com.google.gson.annotations.SerializedName

class Rows {

    @SerializedName("rating"    )
    var rating    : String?   = null

    @SerializedName("comment"   )
    var comment   : String?   = null

    @SerializedName("createdAt" )
    var createdAt : String?   = null

    @SerializedName("Customer"  )
    var Customer  : Customer? = Customer()
}