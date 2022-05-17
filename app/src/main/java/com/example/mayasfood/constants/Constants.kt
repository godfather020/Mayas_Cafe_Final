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
        internal const val TRANS_HISTORY = "v1/myTransactions"
        internal const val NOTIFICATION = "customer/notifications"
        internal const val ADD_WALLET_BALANCE = "v1/addWallet"
        internal const val DEDUCT_WALLET_BALANCE = "v1/withdraw"
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
    var poptToMainCat = 0
    @JvmField
    var categoryName = ""
    const val UserProfile_Path = "http://18.118.2.222/mayas/mayasgarden/assets/images/"
    const val UserCoupon_Path = "http://18.118.2.222/mayas/mayasgarden/assets/images/coupons/"
    const val UserProduct_Path = "http://18.118.2.222/mayas/mayasgarden/assets/images/products/"
    const val userNameError = "Enter a valid name"
    const val emptyFieldError = "Field can't be empty"
    const val phoneNumError = "Enter a valid phone number"
    @JvmField
    var cc = "+1"
    const val duration = 60
    var foodImage = intArrayOf(
        R.drawable.group_117,
        R.drawable.group_118,
        R.drawable.group_119,
        R.drawable.group_120,
        R.drawable.group_117,
        R.drawable.group_118,
        R.drawable.group_119,
        R.drawable.group_120,
        R.drawable.group_117
    )
    @JvmField
    var toppingImage = intArrayOf(
        R.drawable.top1__1_,
        R.drawable.top2__1_,
        R.drawable.top3__1_,
        R.drawable.top4__1_,
        R.drawable.top5__1_
    )
    var imgFood = intArrayOf(
        R.drawable.do_011318_alc_kapoleichinese_lt001_file_shrimp,
        R.drawable.jackfruit_bowl,
        R.drawable.png_clipart_seafood__1_
    )
    var foodimg = intArrayOf(
        R.drawable.pngwing_com,
        R.drawable.png_clipart_vegetarian_cuisine_breakfast_spring__1_,
        R.drawable.unnamed
    )
}