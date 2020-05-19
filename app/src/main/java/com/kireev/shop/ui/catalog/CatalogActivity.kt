package com.kireev.shop.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.kireev.shop.App
import com.kireev.shop.R
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.domain.model.remote.RemoteCategory
import com.kireev.shop.presenter.catalog.CatalogPresenterFactory
import com.kireev.shop.presenter.catalog.CatalogView
import com.kireev.shop.ui.detailed.DetailedActivity
import com.kireev.shop.ui.detailed.DetailedActivity.Companion.DETAILED_LAUNCHED_FROM
import com.kireev.shop.ui.detailed.DetailedActivity.Companion.DETAILED_PRODUCT_KEY
import com.kireev.shop.ui.cart.CartActivity
import kotlinx.android.synthetic.main.activity_catalog.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class CatalogActivity : MvpAppCompatActivity(),
    CatalogView {


    @Inject
    lateinit var catalogPresenterFactory: CatalogPresenterFactory

    private val presenter by moxyPresenter { catalogPresenterFactory.create(category) }

    private lateinit var category: RemoteCategory

    private val catalogAdapter = CatalogAdapter(
        onItemClick = { product -> presenter.onItemClicked(product) },
        onAddToCartClick = { product -> presenter.addProductToCart(product) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        category = intent.getParcelableExtra(CATEGORY_TAG)!!
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)
        setListeners()
        setUpProductsRecycler()
    }

    private fun setListeners() {
        catalogBackIv.setOnClickListener {
            finish()
        }
        catalogOpenCartIv.setOnClickListener {
            presenter.onCartClicked()
        }
        catalogSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onQueryChanged(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.onQueryChanged(newText)
                return true
            }
        })
    }

    private fun setUpProductsRecycler() {
        catalogProductsRv.apply {
            layoutManager = GridLayoutManager(this@CatalogActivity, 2)
            adapter = catalogAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResume()
    }

    override fun startDetailedActivity(product: Product) {
        val intent = Intent(this, DetailedActivity::class.java)
        intent.putExtra(DETAILED_PRODUCT_KEY, product)
        intent.putExtra(DETAILED_LAUNCHED_FROM, this::class.java.simpleName)
        startActivity(intent)
    }

    override fun startCartActivity() {
        startActivity(Intent(this, CartActivity::class.java))
    }


    override fun setProductList(products: List<Product>) {
        catalogAdapter.setList(products)
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
    }

    override fun setCartCountLabelVisibility(visible: Boolean) {
        catalogCartItemsCountTv.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setCartCountText(text: String) {
        catalogCartItemsCountTv.text = text
    }

    override fun setCategoryTitle(title: String) {
        catalogHeaderLabel.text = title
    }

    companion object {
        const val CATEGORY_TAG = "CATEGORY_TAG"
    }
}
