package com.kireev.shop.presenter.catalog

import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.domain.model.local.ProductFactory
import com.kireev.shop.domain.model.remote.RemoteCategory
import com.kireev.shop.presenter.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class CatalogPresenter(
    private val cartItemsDao: CartItemsDao,
    private val category: RemoteCategory
) : BasePresenter<CatalogView>() {

    private lateinit var allProducts: List<Product>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val factory = ProductFactory()
        allProducts = category.products.map { factory.createProduct(it) }
        viewState.setProductList(allProducts)
        viewState.setCategoryTitle(category.name)
    }

    fun onViewResume() {
        updateCartItemsCount()
    }

    fun onQueryChanged(query: String?) {
        if (query.isNullOrEmpty()) {
            viewState.setProductList(allProducts)
        } else {
            val searchResults =
                allProducts.mapNotNull { if (it.productName.contains(query, true)) it else null }
            viewState.setProductList(searchResults)
        }
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

    fun addProductToCart(product: Product) {
        cartItemsDao.addProduct(product)
        updateCartItemsCount()
    }

    fun onItemClicked(product: Product) {
        viewState.startDetailedActivity(product)
    }
}


class CatalogPresenterFactory @Inject constructor(
    private val cartItemsDao: CartItemsDao
) {
    fun create(category: RemoteCategory): CatalogPresenter {
        return CatalogPresenter(
            cartItemsDao,
            category
        )
    }
}