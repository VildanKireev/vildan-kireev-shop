package com.kireev.shop.data

import android.content.SharedPreferences
import com.kireev.shop.domain.dao.CartItemsDao
import com.kireev.shop.domain.model.local.CartItem
import com.kireev.shop.domain.model.local.Product
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

class CartItemsDaoImpl(private val sharedPreferences: SharedPreferences) :
    CartItemsDao {

    private var cartItems: List<CartItem>
        get() {
            val listString = sharedPreferences.getString(CART_ITEMS_TAG, null)
            return listString?.let { Json.parse(CartItem.serializer().list, it) } ?: listOf()
        }
        set(value) {
            val newListString = Json.stringify(CartItem.serializer().list, value)
            sharedPreferences.edit().putString(CART_ITEMS_TAG, newListString).apply()
        }

    override fun clearCart() {
        cartItems = listOf()
    }


    override fun addProduct(product: Product) {
        val items = cartItems
        val item = items.firstOrNull { it.product == product }
        if (item != null) {
            item.count += 1
            cartItems = items
        } else {
            val newItems = mutableListOf<CartItem>().apply {
                addAll(items)
                add(CartItem(product))
            }
            cartItems = newItems
        }
    }

    override fun setAllItems(items: List<CartItem>) {
        cartItems = items
    }

    override fun getAllItems(): List<CartItem> = cartItems

    override fun getItemsCount(): Int = cartItems.size

    companion object {
        private const val CART_ITEMS_TAG = "CART_ITEMS"
    }
}