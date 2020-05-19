package com.kireev.shop.presenter.detailed

import com.kireev.shop.commonPriceFormat
import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.dao.ViewedProductsDao
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.presenter.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class DetailedPresenter(
    private val cartItemsDao: CartItemsDao,
    private val viewedProductsDao: ViewedProductsDao,
    private val product: Product,
    private val launchedFromCart: Boolean
) : BasePresenter<DetailedView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setProductInfo()
        viewedProductsDao.addProduct(product)
    }

    private fun setProductInfo() {
        viewState.setProductName(product.productName)
        viewState.setProductDescription(product.description)
        viewState.setProductAttributes(
            product.attributes.joinToString(
                separator = "\n",
                transform = {
                    it
                })
        )
        if (product.salePercent != 0) {
            viewState.setProductPrice(
                product.discountPrice.commonPriceFormat,
                product.price.commonPriceFormat
            )
        } else {
            viewState.setProductPrice(product.price.commonPriceFormat)
        }
        viewState.loadProductImage(product.imageUrl)
        if (launchedFromCart) {
            viewState.setCartWidgetsInvisible()
        }
    }

    fun onViewResume() {
        if (!launchedFromCart) updateCartItemsCount()
    }

    private fun updateCartItemsCount() {
        val itemsCount = cartItemsDao.getItemsCount()
        if (itemsCount > 0) {
            val formattedCount = if (itemsCount < 10) itemsCount.toString() else "9+"
            viewState.setCartCountLabelVisibility(true)
            viewState.setCartCountText(formattedCount)
        } else {
            viewState.setCartCountLabelVisibility(false)
        }
    }

    fun onCartClicked() {
        viewState.startCartActivity()
    }

    fun onAddToCartClick(product: Product) {
        cartItemsDao.addProduct(product)
        if (!launchedFromCart) updateCartItemsCount()
    }
}


class DetailedPresenterFactory @Inject constructor(
    private val cartItemsDao: CartItemsDao,
    private val viewedProductsDao: ViewedProductsDao
) {
    fun create(product: Product, launchedFromCart: Boolean): DetailedPresenter {
        return DetailedPresenter(
            cartItemsDao,
            viewedProductsDao,
            product,
            launchedFromCart
        )
    }
}