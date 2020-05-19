package com.kireev.shop.presenter.cart

import com.kireev.shop.commonPriceFormat
import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.model.local.CartItem
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CartPresenter @Inject constructor(
    private val cartItemsDao: CartItemsDao
) : MvpPresenter<CartView>() {

    private var cartItems: MutableList<CartItem> = cartItemsDao.getAllItems().toMutableList()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateCartItemsView()
    }

    fun onViewPause() {
        cartItemsDao.setAllItems(cartItems)
    }

    fun onViewResume() {
        cartItems = cartItemsDao.getAllItems().toMutableList()
        updateCartItemsView()
    }

    fun onCartItemDeleteClick(pos: Int) {
        cartItems.removeAt(pos)
        updateCartItemsView()
    }

    fun onItemCountPlusClick(pos: Int): Int {
        cartItems[pos].count += 1
        updateCartTotalPrice()
        return cartItems[pos].count
    }

    fun onItemCountMinusClick(pos: Int): Int {
        cartItems[pos].apply {
            if (count > 1) {
                count -= 1
                updateCartTotalPrice()
            }
        }
        return cartItems[pos].count
    }

    private fun updateCartItemsView() {
        viewState.setNewCartItems(cartItems.map { it.copy() })
        updateCheckoutButton()
        updateCartEmptyMessage()
        updateCartTotalPrice()
    }

    fun onCheckoutClick() {
        viewState.startCheckoutActivity()
    }

    fun onItemClick(pos: Int) {
        viewState.startDetailedActivity(cartItems[pos].product)
    }

    private fun updateCheckoutButton() {
        viewState.setCheckoutButtonEnabled(cartItems.isNotEmpty())
    }

    private fun updateCartEmptyMessage() {
        viewState.setEmptyMsgVisibility(cartItems.isEmpty())
    }

    private fun updateCartTotalPrice() {
        val totalPrice = cartItems.sumByDouble { it.count * it.product.discountPrice }
        viewState.setTotalPriceText(totalPrice.commonPriceFormat)
    }
}

