package com.kireev.shop.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kireev.shop.App
import com.kireev.shop.R
import com.kireev.shop.domain.model.local.CartItem
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.presenter.cart.CartPresenter
import com.kireev.shop.presenter.cart.CartView
import com.kireev.shop.ui.checkout.CheckoutActivity
import com.kireev.shop.ui.detailed.DetailedActivity
import com.kireev.shop.ui.detailed.DetailedActivity.Companion.DETAILED_LAUNCHED_FROM
import com.kireev.shop.ui.detailed.DetailedActivity.Companion.DETAILED_PRODUCT_KEY
import kotlinx.android.synthetic.main.activity_cart.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class CartActivity : MvpAppCompatActivity(),
    CartView {

    @Inject
    lateinit var cartPresenter: CartPresenter

    private val presenter: CartPresenter by moxyPresenter {
        cartPresenter
    }

    private val cartAdapter: CartAdapter =
        CartAdapter(
            object : CartItemClickCallback {
                override fun onDeleteClick(pos: Int) {
                    presenter.onCartItemDeleteClick(pos)
                }

                override fun onCountPlusClick(pos: Int): Int = presenter.onItemCountPlusClick(pos)

                override fun onCountMinusClick(pos: Int): Int = presenter.onItemCountMinusClick(pos)

                override fun onItemClick(pos: Int) {
                    presenter.onItemClick(pos)
                }
            }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setListeners()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        cartItemsRv.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
        ItemTouchHelper(
            CartSwipeCallback(
                onItemSwiped = { pos ->
                    presenter.onCartItemDeleteClick(pos)
                },
                adapter = cartAdapter,
                icon = getDrawable(R.drawable.ic_delete)!!,
                background = getDrawable(R.drawable.background_delete_rounded_red)!!
            )
        ).also { it.attachToRecyclerView(cartItemsRv) }
    }


    private fun setListeners() {
        cartBackIv.setOnClickListener {
            finish()
        }
        cartCheckoutBtn.setOnClickListener {
            presenter.onCheckoutClick()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResume()
    }

    override fun startCheckoutActivity() {
        startActivity(Intent(this, CheckoutActivity::class.java))
    }

    override fun startDetailedActivity(product: Product) {
        val intent = Intent(this, DetailedActivity::class.java)
        intent.putExtra(DETAILED_PRODUCT_KEY, product)
        intent.putExtra(DETAILED_LAUNCHED_FROM, this::class.java.simpleName)
        startActivity(intent)
    }

    override fun setCheckoutButtonEnabled(enabled: Boolean) {
        cartCheckoutBtn.isEnabled = enabled
        if (enabled) {
            cartCheckoutBtn.setBackgroundResource(R.drawable.background_button_rounded_black)
        } else {
            cartCheckoutBtn.setBackgroundResource(R.drawable.background_button_disabled)
        }
    }

    override fun setEmptyMsgVisibility(visible: Boolean) {
        cartEmptyMsgTv.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setNewCartItems(items: List<CartItem>) {
        cartAdapter.setList(items)
    }

    override fun smoothScrollToPosition(pos: Int) {
        cartItemsRv.smoothScrollToPosition(pos)
    }

    override fun setTotalPriceText(text: String) {
        cartTotalPrice.text = text
    }
}
