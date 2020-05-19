package com.kireev.shop

import android.app.Application
import com.kireev.shop.di.AppComponent
import com.kireev.shop.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}