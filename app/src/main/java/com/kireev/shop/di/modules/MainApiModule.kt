package com.kireev.shop.di.modules

import com.kireev.shop.domain.MainApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MainApiModule {

    @Provides
    @Singleton
    fun provideMainApi(): MainApi = Retrofit.Builder()
        .baseUrl("http://207.254.71.167:9191/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MainApi::class.java)
}