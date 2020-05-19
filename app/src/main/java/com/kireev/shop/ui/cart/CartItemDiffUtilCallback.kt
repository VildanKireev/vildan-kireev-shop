package com.kireev.shop.ui.cart

import androidx.recyclerview.widget.DiffUtil
import com.kireev.shop.domain.model.local.CartItem


class CartItemDiffUtilCallback(
    private val oldList: List<CartItem>,
    private val newList: List<CartItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldItem: CartItem = oldList[oldItemPosition]
        val newItem: CartItem = newList[newItemPosition]
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldItem: CartItem = oldList[oldItemPosition]
        val newItem: CartItem = newList[newItemPosition]
        return oldItem.count == newItem.count && oldItem.product == newItem.product
    }

}