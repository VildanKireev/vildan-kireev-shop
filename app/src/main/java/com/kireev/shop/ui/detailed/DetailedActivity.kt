package com.kireev.shop.ui.detailed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kireev.shop.App
import com.kireev.shop.R
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.presenter.detailed.DetailedPresenterFactory
import com.kireev.shop.presenter.detailed.DetailedView
import com.kireev.shop.strikethroughSpannable
import com.kireev.shop.ui.cart.CartActivity
import kotlinx.android.synthetic.main.activity_detailed.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class DetailedActivity : MvpAppCompatActivity(),
    DetailedView {

    @Inject
    lateinit var detailedPresenter: DetailedPresenterFactory

    private val presenter by moxyPresenter { detailedPresenter.create(product, launchedFromCart) }

    lateinit var product: Product

    private var launchedFromCart: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        product = intent?.getParcelableExtra(DETAILED_PRODUCT_KEY)!!
        launchedFromCart = intent.getStringExtra(DETAILED_LAUNCHED_FROM)
            ?.let { it == CartActivity::class.java.simpleName } ?: false
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        setListeners()
    }

    private fun setListeners() {
        detailedBackIv.setOnClickListener {
            finish()
        }
        detailedOpenCartIv.setOnClickListener {
            presenter.onCartClicked()
        }
        detailedAddToCartBtn.setOnClickListener {
            presenter.onAddToCartClick(product)

        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResume()
    }


    override fun startCartActivity() {
        startActivity(Intent(this, CartActivity::class.java))
    }

    override fun loadProductImage(imgUrl: String) {
        Glide
            .with(detailedPicIv.context)
            .load(imgUrl)
            .error(R.mipmap.ic_launcher)
            .into(detailedPicIv)
    }

    override fun setProductDescription(description: String) {
        detailedDescriptionTv.text = description
    }

    override fun setProductAttributes(text: String) {
        detailedAttributesTv.text = text
    }

    override fun setProductPrice(mainPrice: String) {
        detailedMainPriceTv.text = mainPrice
    }

    override fun setProductPrice(discountPrice: String, rawPrice: String) {
        detailedMainPriceTv.text = discountPrice
        detailedRawPriceTv.apply {
            visibility = View.VISIBLE
            text = rawPrice.strikethroughSpannable
        }
    }

    override fun setProductName(name: String) {
        detailedNameTv.text = name
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
    }


    override fun setCartCountLabelVisibility(visible: Boolean) {
        detailedCartItemsCountTv.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setCartCountText(text: String) {
        detailedCartItemsCountTv.text = text
    }

    override fun setCartWidgetsInvisible() {
        detailedCartItemsCountTv.visibility = View.GONE
        detailedOpenCartIv.visibility = View.GONE
    }


    companion object {
        const val DETAILED_PRODUCT_KEY = "DETAILED_PRODUCT_KEY"
        const val DETAILED_LAUNCHED_FROM = "DETAILED_LAUNCHED_FROM"
    }
}
