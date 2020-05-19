package com.kireev.shop.domain.dao

import com.kireev.shop.domain.model.local.Product

interface ViewedProductsDao {
    /**
     * сохранить этот [product] как просмотренный
     */
    fun addProduct(product: Product)

    /**
     * сохранить все из списка [products] как просмотренные
     */
    fun addProducts(products: List<Product>)

    /**
     * получить все просмотренные товары
     * может быть пустым
     */
    fun getAllProducts(): List<Product>
}