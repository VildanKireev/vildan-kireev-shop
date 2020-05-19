package com.kireev.shop.presenter.categories

import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.dao.ViewedProductsDao
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.domain.model.remote.RemoteCategory
import com.kireev.shop.domain.model.remote.RemoteProduct
import com.kireev.shop.presenter.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject


@InjectViewState
class CategoriesPresenter(
    private val cartItemsDao: CartItemsDao,
    private val viewedProductsDao: ViewedProductsDao,
    private val categoriesList: List<RemoteCategory>
) : BasePresenter<CategoriesView>() {

    private val categories = mutableListOf<RemoteCategory>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (categoriesList.sumBy { it.products.size } > 0) {
            val allProducts = mutableListOf<RemoteProduct>()
            val discountProducts = mutableListOf<RemoteProduct>()
            categoriesList.forEach { category ->
                categories.add(category)
                allProducts.addAll(category.products)
                category.products.forEach { product ->
                    if (product.discountPercent > 0) discountProducts.add(product)
                }
            }
            categories.add(RemoteCategory("Все оружие", allProducts))
            categories.add(RemoteCategory("Оружие со скидкой", discountProducts))
        }
        viewState.setCategoriesList(categories.map { it.name })
    }

    private fun updateViewedItems() {
        val viewedItems = viewedProductsDao.getAllProducts()
        viewState.setViewedProductsVisibility(viewedItems.isNotEmpty())
        if (viewedItems.isNotEmpty()) {
            viewState.setViewedItems(viewedItems)
        }
    }


    fun onViewResume() {
        updateCartItemsCount()
        updateViewedItems()
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

    fun onCategoryClick(pos: Int) {
        viewState.startCatalogActivity(categories[pos])
    }

    fun onCartClick() {
        viewState.startCartActivity()
    }

    fun onViewedItemClick(product: Product) {
        viewState.startDetailedActivity(product)
    }
}

class CategoriesPresenterFactory @Inject constructor(
    private val cartItemsDao: CartItemsDao,
    private val viewedProductsDao: ViewedProductsDao
) {
    fun create(categoriesList: List<RemoteCategory>): CategoriesPresenter {
        return CategoriesPresenter(
            cartItemsDao,
            viewedProductsDao,
            categoriesList
        )
    }
}
