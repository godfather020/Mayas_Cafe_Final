package com.example.mayasfood.Retrofite.response

import com.google.gson.annotations.SerializedName

class Product {

    @SerializedName("id"             )
    var id             : Int?                     = null

    @SerializedName("productName"    )
    var productName    : String?                  = null

    @SerializedName("productDesc"    )
    var productDesc    : String?                  = null

    @SerializedName("productPic"     )
    var productPic     : String?                  = null

    @SerializedName("categoryId"     )
    var categoryId     : Int?                     = null

    @SerializedName("branchId"       )
    var branchId       : Int?                     = null

    @SerializedName("subcategoryId"  )
    var subcategoryId  : Int?                     = null

    @SerializedName("status"         )
    var status         : Boolean?                 = null

    @SerializedName("customerrating" )
    var customerrating : String?                  = null

    @SerializedName("systemrating"   )
    var systemrating   : String?                  = null

    @SerializedName("createdBy"      )
    var createdBy      : String?                  = null

    @SerializedName("updatedBy"      )
    var updatedBy      : String?                  = null

    @SerializedName("createdAt"      )
    var createdAt      : String?                  = null

    @SerializedName("updatedAt"      )
    var updatedAt      : String?                  = null

    @SerializedName("Productprices"  )
    var Productprices  : ArrayList<Productprices> = arrayListOf()

   /* @SerializedName("Topings"        )
    var Topings        : ArrayList<String>        = arrayListOf()*/

}