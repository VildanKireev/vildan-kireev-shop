package com.kireev.shop.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.bumptech.glide.Glide
import com.kireev.shop.R
import com.kireev.shop.commonPriceFormat
import com.kireev.shop.domain.model.local.CartItem
import com.kireev.shop.strikethroughSpannable
import kotlinx.android.synthetic.main.item_cart_layout.view.*

class CartAdapter(
    val callbackDelegate: CartItemClickCallback
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var items: List<CartItem> = listOf<CartItem>()

    fun setList(cartItems: List<CartItem>) {
        val diffResult = DiffUtil.calculateDiff(
            CartItemDiffUtilCallback(
                items,
                cartItems
            )
        )
        items = cartItems.toList()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart_layout, parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cartItem: CartItem) {
            loadProductImage(cartItem.product.imageUrl)
            setProductInfo(cartItem)
            setListeners()
        }

        private fun setProductInfo(cartItem: CartItem) {
            val product = cartItem.product
            itemView.apply {
                cartItemName.text = product.productName
                cartItemCountTv.text = cartItem.count.toString()
                if (product.salePercent != 0) {
                    cartItemDiscountPrice.visibility = View.VISIBLE
                    cartItemDiscountPrice.text = product.discountPrice.commonPriceFormat
                    cartItemMainPrice.text =
                        product.price.commonPriceFormat.strikethroughSpannable
                } else {
                    cartItemMainPrice.text = product.price.commonPriceFormat
                    cartItemDiscountPrice.visibility = View.GONE
                }
            }
        }

        private fun loadProductImage(imageUrl: String) {
            Glide.with(itemView.cartItemPicIv.context)
                .load(imageUrl)
                .error(R.drawable.ic_catalog_item_stub)
                .into(itemView.cartItemPicIv)
        }

        private fun setListeners() {
            itemView.apply {
                cartItemDeleteLl.setOnClickListener {
                    if (adapterPosition != NO_POSITION) {
                        callbackDelegate.onDeleteClick(adapterPosition)
                    }
                }
                cartItemCountMinusIv.setOnClickListener {
                    if (adapterPosition != NO_POSITION) {
                        val newCount = callbackDelegate.onCountMinusClick(adapterPosition)
                        cartItemCountTv.text = newCount.toString()
                    }
                }
                cartItemCountPlusIv.setOnClickListener {
                    if (adapterPosition != NO_POSITION) {
                        val newCount = callbackDelegate.onCountPlusClick(adapterPosition)
                        cartItemCountTv.text = newCount.toString()
                    }
                }
                itemRootCl.setOnClickListener {
                    if (adapterPosition != NO_POSITION) {
                        callbackDelegate.onItemClick(adapterPosition)
                    }
                }
            }
        }

    }


}