package com.kireev.shop.presenter.launch

import com.kireev.shop.R
import com.kireev.shop.domain.MainApi
import com.kireev.shop.presenter.BasePresenter
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.InjectViewState
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@InjectViewState
class LaunchPresenter @Inject constructor(
    private val mainApi: MainApi
) : BasePresenter<LaunchView>() {

    private val splashDelayMilliseconds: Long = 1500

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch {
            val categories =
                async { mainApi.allProductsWithCategories() }
            delay(splashDelayMilliseconds)
            viewState.startCategoriesActivity(categories.await())
        }
    }

    override fun onFailure(e: Throwable) {
        super.onFailure(e)
        when (e) {
            is ConnectException -> {
                viewState.showMessage(R.string.err_connection_error)
            }
            is UnknownHostException -> {
                viewState.showMessage(R.string.err_connection_error)
            }
        }
        viewState.startCategoriesActivity(listOf())
    }
}