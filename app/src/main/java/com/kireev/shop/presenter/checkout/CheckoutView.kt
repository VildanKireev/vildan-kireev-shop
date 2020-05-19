package com.kireev.shop.presenter.checkout

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CheckoutView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showErrorLastName(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showErrorFirstName(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showErrorPhone(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRawPrice(formattedPrice: String)

    @StateStrategyType(SkipStrategy::class)
    fun showMessage(stringResId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDiscount(formattedDiscount: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTotalPrice(formattedPrice: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCartItemsCount(count: Int)

    @StateStrategyType(SkipStrategy::class)
    fun closeView()
}