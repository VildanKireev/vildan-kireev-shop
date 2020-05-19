package com.kireev.shop.presenter.catalog

import com.kireev.shop.domain.model.local.Product
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CatalogView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun startDetailedActivity(product: Product)

    @StateStrategyType(SkipStrategy::class)
    fun startCartActivity()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProductList(products: List<Product>)

    @StateStrategyType(SkipStrategy::class)
    fun showMessage(stringResId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCartCountLabelVisibility(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCartCountText(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCategoryTitle(title: String)
}