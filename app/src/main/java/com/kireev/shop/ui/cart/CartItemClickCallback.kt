package com.kireev.shop.ui.cart

interface CartItemClickCallback {

    fun onDeleteClick(pos: Int)

    fun onItemClick(pos: Int)

    /**
     * returns new count
     */
    fun onCountPlusClick(pos: Int): Int

    /**
     * returns new count
     */
    fun onCountMinusClick(pos: Int): Int
}