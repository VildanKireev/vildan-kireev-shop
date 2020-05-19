package com.kireev.shop.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.kireev.shop.data.CartItemsDaoImpl
import com.kireev.shop.data.ViewedProductDaoImpl
import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.dao.ViewedProductsDao
import dagger.Module
import dagger.Provides

@Module
class PreferencesModule {

    @Provides
    fun providePreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("data", Context.MODE_PRIVATE)

    @Provides
    fun provideCartItems(preferences: SharedPreferences): CartItemsDao =
        CartItemsDaoImpl(preferences)

    @Provides
    fun provideViewedProducts(preferences: SharedPreferences): ViewedProductsDao =
        ViewedProductDaoImpl(preferences)
}