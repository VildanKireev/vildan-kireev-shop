package com.kireev.shop.ui.checkout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.kireev.shop.App
import com.kireev.shop.presenter.checkout.CheckoutPresenter
import com.kireev.shop.presenter.checkout.CheckoutView
import com.kireev.shop.R
import com.kireev.shop.domain.model.remote.RemoteOrder
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.activity_checkout.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class CheckoutActivity : MvpAppCompatActivity(),
    CheckoutView {


    @Inject
    lateinit var checkoutPresenter: CheckoutPresenter

    private val presenter by moxyPresenter { checkoutPresenter }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        presenter.attachView(this)
        setListeners()
    }

    private fun setListeners() {
        checkoutPaymentMethodGroup.setOnCheckedChangeListener { _, paymentTypeId ->
            val paymentType = when (paymentTypeId) {
                R.id.checkoutRadioCard -> RemoteOrder.PaymentType.CardOnReceiving
                else -> RemoteOrder.PaymentType.CashOnReceiving
            }
            presenter.onPaymentMethodChanged(paymentType)
        }
        checkoutBackIv.setOnClickListener {
            finish()
        }
        checkoutMakeOrder.setOnClickListener {
            presenter.onMakeOrderClick()
        }
        checkoutFirstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.onFirstNameChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        checkoutLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.onLastNameChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        MaskedTextChangedListener.installOn(
            checkoutPhoneNumber,
            "+7 ([000]) [000]-[00]-[00]",
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    presenter.onPhoneNumberChanged("+7$extractedValue")
                }
            }
        )
    }


    private fun EditText.showErrorIcon(visible: Boolean) {
        val drawable = if (visible) R.drawable.ic_error else 0
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    override fun showErrorLastName(visible: Boolean) {
        checkoutLastName.showErrorIcon(visible)
    }

    override fun showErrorFirstName(visible: Boolean) {
        checkoutFirstName.showErrorIcon(visible)
    }


    override fun showErrorPhone(visible: Boolean) {
        checkoutPhoneNumber.showErrorIcon(visible)
    }

    override fun setRawPrice(formattedPrice: String) {
        checkoutRawPrice.text = formattedPrice
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
    }

    override fun setDiscount(formattedDiscount: String) {
        checkoutDiscountAmount.text = formattedDiscount
    }

    override fun setTotalPrice(formattedPrice: String) {
        checkoutTotalPrice.text = formattedPrice
    }

    override fun setCartItemsCount(count: Int) {
        checkoutProductsCount.text = getString(R.string.checkout_items_count, count)
    }

    override fun closeView() {
        finish()
    }
}


