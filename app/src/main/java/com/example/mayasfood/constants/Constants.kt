package com.example.mayasfood.constants

import com.example.mayasfood.R

object Constants {
    //Base URLs
    const val API_DEVELOPMENT_URL = "http://18.118.2.222:3000/api/"
    const val API_TESTING_URL = "testingURL"
    const val API_LIVE_URL = "liveURL"

    object ApiConstant {

        //API END POINTS
        internal const val LOGIN = "public/login"
        internal const val VERIFY = "public/verify"
        internal const val REGISTER = "public/register"
        internal const val LOTTERIES = "v1/lotteries"
        internal const val TICKETS = "v1/tickets"
        internal const val BUY_TICKETS = "v1/buyTickets"
        internal const val MY_TICKETS = "v1/myTickets"
        internal const val TOP_WINNERS = "v1/topWinners"
        internal const val WALLET_BALANCE = "v1/myWallet"
        internal const val PAYTM_TOKEN = "v1/paytmChecksum"
        internal const val TRANS_HISTORY = "v1/myTransactions"
        internal const val NOTIFICATION = "v1/notifications"
        internal const val ADD_WALLET_BALANCE = "v1/addWallet"
        internal const val DEDUCT_WALLET_BALANCE = "v1/withdraw"
        internal const val SET_PROFILE_IMAGE = "v1/uploadProfilePic"
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
    }


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