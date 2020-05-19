package com.kireev.shop.ui.categories

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kireev.shop.App
import com.kireev.shop.R
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.domain.model.remote.RemoteCategory
import com.kireev.shop.presenter.categories.CategoriesPresenterFactory
import com.kireev.shop.presenter.categories.CategoriesView
import com.kireev.shop.ui.cart.CartActivity
import com.kireev.shop.ui.catalog.CatalogActivity
import com.kireev.shop.ui.catalog.CatalogActivity.Companion.CATEGORY_TAG
import com.kireev.shop.ui.detailed.DetailedActivity
import kotlinx.android.synthetic.main.activity_categories.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class CategoriesActivity : MvpAppCompatActivity(),
    CategoriesView {

    @Inject
    lateinit var categoriesPresenter: CategoriesPresenterFactory

    private val presenter by moxyPresenter { categoriesPresenter.create(categoriesList) }

    private val categoriesAdapter =
        CategoriesAdapter(onCategoryClick = { pos ->
            presenter.onCategoryClick(
                pos
            )
        })

    private val viewedProductsAdapter =
        ViewedProductsAdapter(onItemClick = {
            presenter.onViewedItemClick(
                it
            )
        })

    private lateinit var categoriesList: List<RemoteCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        categoriesList = intent.getParcelableArrayListExtra(CATEGORIES_LIST_TAG)!!
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        setListeners()
        setUpCategoriesRecycler()
        setUpViewedRecycler()
    }

    private fun setUpViewedRecycler() {
        categoriesViewedProductsRv.apply {
            layoutManager =
                LinearLayoutManager(
                    this@CategoriesActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = viewedProductsAdapter
        }
    }

    private fun setUpCategoriesRecycler() {
        categoriesRv.layoutManager = LinearLayoutManager(this)
        categoriesRv.adapter = categoriesAdapter
    }

    private fun setListeners() {
        categoriesOpenCartIv.setOnClickListener {
            presenter.onCartClick()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResume()
    }

    override fun showMessage(stringResId: Int) {
        Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
    }

    override fun setViewedItems(newItems: List<Product>) {
        viewedProductsAdapter.setList(newItems)
    }

    override fun setViewedProductsVisibility(visible: Boolean) {
        categoriesViewedProductsCl.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun startDetailedActivity(product: Product) {
        val intent = Intent(this, DetailedActivity::class.java)
        intent.putExtra(DetailedActivity.DETAILED_PRODUCT_KEY, product)
        intent.putExtra(DetailedActivity.DETAILED_LAUNCHED_FROM, this::class.java.simpleName)
        startActivity(intent)
    }

    override fun setCategoriesList(categories: List<String>) {
        categoriesAdapter.setList(categories)
    }

    override fun startCatalogActivity(category: RemoteCategory) {
        val intent = Intent(this, CatalogActivity::class.java)
        intent.putExtra(CATEGORY_TAG, category)
        startActivity(intent)
    }

    override fun startCartActivity() {
        startActivity(Intent(this, CartActivity::class.java))
    }

    override fun setCartCountLabelVisibility(visible: Boolean) {
        categoriesCartItemsCountTv.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setCartCountText(text: String) {
        categoriesCartItemsCountTv.text = text
    }

    companion object {
        const val CATEGORIES_LIST_TAG = "CATEGORIES_TAG"
    }
}
