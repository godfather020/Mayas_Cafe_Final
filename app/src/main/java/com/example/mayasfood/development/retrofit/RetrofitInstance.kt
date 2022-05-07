package com.example.mayasfood.development.retrofit

import com.example.lottry.data.remote.retrofit.api.Apis
import com.example.mayasfood.BuildConfig
import com.example.mayasfood.constants.Constants
import com.google.android.datatransport.runtime.dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

public class RetrofitInstance {

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.API_DEVELOPMENT_URL)
        .build()
        .create(Apis::class.java)

}

