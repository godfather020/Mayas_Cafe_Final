package com.example.mayasfood.constants

import com.example.mayasfood.R

object Constants {

    //Base URLs
    const val API_DEVELOPMENT_URL = "http://18.118.2.222:3000/api/"
    const val API_TESTING_URL = "testingURL"
    const val API_LIVE_URL = "liveURL"

    object ApiConstant {

        //API END POINTS
        internal const val DASHBOARD = "public/Listhomedetails"
        internal const val VERIFY = "public/verify"
        internal const val REGISTER = "public/register"
        internal const val DEVICE_INFO = "public/deviceInfo"
        internal const val USER_PROFILE = "customer/getprofile"
        internal const val UPDATE_PROFILE = "customer/Updatecustomerprofile"
        internal const val MY_COUPONS = "public/Listcoupon"
        internal const val POPULAR_FOOD = "public/Listpopularproduct"
        internal const val RESTAURANT_CHOICES = "public/Listrestaurentproduct"
        internal const val LIST_PRODUCTS = "public/Listproduct"
        internal const val ADD_REMOVE_FAV = "customer/Createproductfavorite"
        internal const val NOTIFICATION = "customer/notifications"
        internal const val FAVORITE_LIST = "customer/Listfavoriteproduct"
        internal const val CREATE_ORDER = "customer/Createorder"
        internal const val GET_ORDERS = "customer/Listorder"
        internal const val SET_PROFILE_IMAGE = "customer/uploadProfilePic"
        internal const val TODAY_WINNERS = "v1/todaywinninglist"
        internal var DEVICE_TOKEN = ""

    }

    object sharedPrefrencesConstant{

        internal const val DEVICE_TOKEN = "D-token"
        internal const val X_TOKEN="x-token"
        internal const val WALLET_BALANCE="wallet-balance"
        internal const val REFFERAL_AMOUNT="referral-balance"
        internal const val LOGIN="login"
        internal const val FIRST_TIME="first_time"
        internal const val USER_N = "userName"
        internal const val USER_P = "userPhone"
        internal const val USER_I = "userProfile"
        internal const val CATEGORYID = "categoryID"

    }
    @JvmField
    var USER_NAME = "Stranger"
    @JvmField
    var USER_PHONE = ""
    @JvmField
    var USER_TOKEN = ""
    @JvmField
    var onetTime = 1
    @JvmField
    var categoryId = "0"
    @JvmField
    var popBack = 0
    @JvmField
    var currentFrag = "D"
    @JvmField
    var categoryName = ""
    @JvmField
    var add = 0
    @JvmField
    var q = 1
    @JvmField
    var cart_totalItems = 0
    @JvmField
    var productId = ArrayList<String>()
    @JvmField
    var foodImg = ArrayList<String>()
    @JvmField
    var foodName = ArrayList<String>()
    @JvmField
    var foodPrice = ArrayList<Int>()
    @JvmField
    var foodQuantity = ArrayList<Int>()
    @JvmField
    var foodId = ArrayList<Int>()
    @JvmField
    var subTotal = 0
    @JvmField
    var tax = 0
    @JvmField
    var total = 0
    @JvmField
    var singleID = ""
    const val UserProfile_Path = "http://18.118.2.222/mayas/mayasgarden/assets/images/"
    const val UserCoupon_Path = "http://18.118.2.222/mayas/mayasgarden/assets/images/coupons/"
    const val UserProduct_Path = "http://18.118.2.222/mayas/mayasgarden/assets/images/products/"
    const val userNameError = "Enter a valid name"
    const val emptyFieldError = "Field can't be empty"
    const val phoneNumError = "Enter a valid phone number"
    @JvmField
    var cc = "+1"
    const val duration = 60

}