package com.kireev.shop.data

import android.content.SharedPreferences
import com.kireev.shop.domain.dao.ViewedProductsDao
import com.kireev.shop.domain.model.local.Product
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

class ViewedProductDaoImpl(
    private val sharedPreferences: SharedPreferences
) : ViewedProductsDao {

    private var savedProducts: List<Product>
        get() {
            val listString = sharedPreferences.getString(VIEWED_PRODUCTS_TAG, null)
            return listString?.let { Json.parse(Product.serializer().list, it) } ?: listOf()
        }
        set(value) {
            val newListString = Json.stringify(Product.serializer().list, value)
            sharedPreferences.edit().putString(VIEWED_PRODUCTS_TAG, newListString).apply()
        }

    override fun addProduct(product: Product) {
        val oldProductList: List<Product> = savedProducts
        val newProductList = mutableListOf<Product>().apply {
            add(product)
            addAll(oldProductList.filter { it != product })
        }
        savedProducts = newProductList
    }

    override fun addProducts(products: List<Product>) {
        val oldProductList: List<Product> = savedProducts
        val newProductList = mutableListOf<Product>().apply {
            addAll(products)
            addAll(oldProductList.filter { !products.contains(it) })
        }
        savedProducts = newProductList
    }

    override fun getAllProducts(): List<Product> {
        return savedProducts
    }

    companion object {
        private const val VIEWED_PRODUCTS_TAG = "VIEWED_PRODUCTS_LIST"
    }
}