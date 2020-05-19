package com.kireev.shop.di

import android.content.Context
import com.kireev.shop.di.modules.MainApiModule
import com.kireev.shop.di.modules.PreferencesModule
import com.kireev.shop.ui.cart.CartActivity
import com.kireev.shop.ui.catalog.CatalogActivity
import com.kireev.shop.ui.categories.CategoriesActivity
import com.kireev.shop.ui.checkout.CheckoutActivity
import com.kireev.shop.ui.detailed.DetailedActivity
import com.kireev.shop.ui.launch.LaunchScreenActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        PreferencesModule::class,
        MainApiModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(activity: LaunchScreenActivity)
    fun inject(activity: CategoriesActivity)
    fun inject(activity: DetailedActivity)
    fun inject(activity: CatalogActivity)
    fun inject(activity: CartActivity)
    fun inject(activity: CheckoutActivity)
}