package com.kireev.shop.domain.dao

import com.kireev.shop.domain.model.local.CartItem
import com.kireev.shop.domain.model.local.Product

interface CartItemsDao {

    /**
     * удалить все товары из корзины
     */
    fun clearCart()

    /**
     * добавить новый CartItem с [product] в корзину
     * если CartItem с таким [product] уже существует, то увеличивается его количество
     */
    fun addProduct(product: Product)

    /**
     * удалить старые [items] и установить новые [items]
     */
    fun setAllItems(items: List<CartItem>)

    /**
     * получить все [items] корзины
     * может быть пустым
     */
    fun getAllItems(): List<CartItem>

    /**
     * возвращает количество уникальных товаров в корзине, а не сумму всех
     */
    fun getItemsCount(): Int
}