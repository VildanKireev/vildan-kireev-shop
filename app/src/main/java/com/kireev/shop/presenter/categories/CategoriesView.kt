package com.kireev.shop.presenter.categories

import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.domain.model.remote.RemoteCategory
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CategoriesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCategoriesList(categories: List<String>)

    @StateStrategyType(SkipStrategy::class)
    fun startCatalogActivity(category: RemoteCategory)

    @StateStrategyType(SkipStrategy::class)
    fun startCartActivity()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCartCountLabelVisibility(visible: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCartCountText(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun showMessage(stringResId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setViewedItems(newItems: List<Product>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setViewedProductsVisibility(visible: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun startDetailedActivity(product: Product)

}