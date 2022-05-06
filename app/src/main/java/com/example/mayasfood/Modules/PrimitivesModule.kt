package com.example.mayasfood.Modules

import com.example.mayasfood.BuildConfig
import com.example.mayasfood.constants.Constants
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import javax.inject.Named

@Module
class PrimitivesModule {

    @Provides
    @Named(Constants.API_DEVELOPMENT_URL)
    fun provideDevelopmentURL(): String = BuildConfig.URL_DEVELOPMENT

    @Provides
    @Named(Constants.API_LIVE_URL)
    fun provideLiveURL(): String = BuildConfig.URL_LIVE

    @Provides
    @Named(Constants.API_TESTING_URL)
    fun provideTestingURL(): String = BuildConfig.URL_TESTING
}