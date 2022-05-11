package com.example.mayasfood.Retrofite.response

import com.google.gson.annotations.SerializedName

class ListcouponResponce {

    @SerializedName("id"            )
    var id            : Int?     = null

    @SerializedName("branchId"      )
    var branchId      : Int?     = null

    @SerializedName("name"          )
    var name          : String?  = null

    @SerializedName("code"          )
    var code          : String?  = null

    @SerializedName("title"         )
    var title         : String?  = null

    @SerializedName("desc"          )
    var desc          : String?  = null

    @SerializedName("calculateType" )
    var calculateType : String?  = null

    @SerializedName("uptoDiscount"  )
    var uptoDiscount  : String?  = null

    @SerializedName("minimumAmount" )
    var minimumAmount : String?  = null

    @SerializedName("status"        )
    var status        : Boolean? = null

    @SerializedName("startAt"       )
    var startAt       : String?  = null

    @SerializedName("stopAt"        )
    var stopAt        : String?  = null

    @SerializedName("bannerImage"   )
    var bannerImage   : String?  = null

    @SerializedName("createdAt"     )
    var createdAt     : String?  = null

    @SerializedName("updatedAt"     )
    var updatedAt     : String?  = null

}