package com.kireev.shop.presenter.cart

import com.kireev.shop.domain.model.local.CartItem
import com.kireev.shop.domain.model.local.Product
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CartView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNewCartItems(items: List<CartItem>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun smoothScrollToPosition(pos: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTotalPriceText(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun startCheckoutActivity()

    @StateStrategyType(SkipStrategy::class)
    fun startDetailedActivity(product: Product)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCheckoutButtonEnabled(enabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setEmptyMsgVisibility(visible: Boolean)
}