package com.example.mayasfood.Retrofite.response

import com.google.gson.annotations.SerializedName

class RatingResponce {

    @SerializedName("count" )
    var count : Int?            = null

    @SerializedName("rows"  )
    var rows  : ArrayList<Rows> = arrayListOf()
}