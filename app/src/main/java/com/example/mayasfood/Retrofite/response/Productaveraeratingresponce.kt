package com.example.mayasfood.Retrofite.response

import com.google.gson.annotations.SerializedName

class Productaveraeratingresponce {

    @SerializedName("customerrating" )
    var customerrating : String? = null

    @SerializedName("starone"        )
    var starone        : String? = null

    @SerializedName("startwo"        )
    var startwo        : String? = null

    @SerializedName("startthree"     )
    var startthree     : String? = null

    @SerializedName("startfour"      )
    var startfour      : String? = null

    @SerializedName("starfive"       )
    var starfive       : String? = null
}