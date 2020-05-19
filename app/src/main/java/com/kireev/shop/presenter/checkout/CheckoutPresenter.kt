package com.kireev.shop.presenter.checkout

import com.kireev.shop.R
import com.kireev.shop.checkoutPriceFormat
import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.MainApi
import com.kireev.shop.domain.model.local.CartItem
import com.kireev.shop.domain.model.local.OrderModel
import com.kireev.shop.domain.model.remote.RemoteOrder
import com.kireev.shop.presenter.BasePresenter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@InjectViewState
class CheckoutPresenter @Inject constructor(
    private val mainApi: MainApi,
    private val cartItemsDao: CartItemsDao
) : BasePresenter<CheckoutView>() {

    private lateinit var cartItems: List<CartItem>

    private val model = OrderModel()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setOrderInfo()
    }

    private fun setOrderInfo() {
        cartItems = cartItemsDao.getAllItems()
        viewState.setCartItemsCount(cartItems.sumBy { it.count })
        viewState.setTotalPrice(calcPurchaseDiscountSum().checkoutPriceFormat)
        viewState.setRawPrice(calcPurchaseRawPricesSum().checkoutPriceFormat)
        viewState.setDiscount(calcTotalDiscount().checkoutPriceFormat)
    }

    fun onFirstNameChanged(text: String) {
        model.firstName = text
        viewState.showErrorFirstName(!lengthIsCorrect(text))
    }

    fun onLastNameChanged(text: String) {
        model.lastName = text
        viewState.showErrorLastName(!lengthIsCorrect(text))
    }

    fun onPhoneNumberChanged(text: String) {
        model.phoneNumber = text
        viewState.showErrorPhone(!numberIsCorrect(text))
    }

    fun onPaymentMethodChanged(newPaymentType: RemoteOrder.PaymentType) {
        model.paymentType = newPaymentType
    }

    fun onMakeOrderClick() {
        when {
            cartItems.isEmpty() -> {
                viewState.showMessage(R.string.err_empty_order)
            }
            orderModelIsCorrect() -> {
                val remoteOrder =
                    RemoteOrder(
                        model.firstName,
                        model.lastName,
                        model.phoneNumber,
                        model.paymentType,
                        cartItems.map { RemoteOrder.Item(it.product.id, it.count) }
                    )
                launch {
                    mainApi.createOrder(remoteOrder)
                    viewState.showMessage(R.string.message_order_processed)
                    cartItemsDao.clearCart()
                    viewState.closeView()
                }
            }
            else -> {
                viewState.showMessage(R.string.err_incorrect_fields_value)
            }
        }
    }

    private fun orderModelIsCorrect(): Boolean {
        return lengthIsCorrect(model.firstName)
                && lengthIsCorrect(model.lastName)
                && numberIsCorrect(model.phoneNumber)
    }

    private fun lengthIsCorrect(text: String): Boolean = text.length > 1

    private fun numberIsCorrect(text: String): Boolean {
        return text.matches(Regex("((\\+7|8)([0-9]){10})"))
    }

    private fun calcPurchaseDiscountSum(): Double =
        cartItems.sumByDouble { it.product.discountPrice * it.count }

    private fun calcPurchaseRawPricesSum(): Double =
        cartItems.sumByDouble { it.product.price * it.count }

    private fun calcTotalDiscount(): Double = calcPurchaseRawPricesSum() - calcPurchaseDiscountSum()

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
    }
}

