package com.kireev.shop.presenter.launch

import com.kireev.shop.domain.model.remote.RemoteCategory
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LaunchView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startCategoriesActivity(categories: List<RemoteCategory>)

    @StateStrategyType(SkipStrategy::class)
    fun showMessage(stringResId: Int)

}