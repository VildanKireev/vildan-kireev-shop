package com.kireev.shop.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kireev.shop.R
import com.kireev.shop.commonPriceFormat
import com.kireev.shop.domain.model.local.Product
import kotlinx.android.synthetic.main.item_viewed_layout.view.*

class ViewedProductsAdapter(
    private val onItemClick: (product: Product) -> Unit
) : RecyclerView.Adapter<ViewedProductsAdapter.ViewHolder>() {

    private var products = listOf<Product>()

    fun setList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewed_layout, parent, false)
    )

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            loadImage(product.imageUrl)
            itemView.apply {
                viewedItemNameTv.text = product.productName
                viewedItemPrice.text = product.discountPrice.commonPriceFormat
            }.also { it.setOnClickListener { onItemClick(products[adapterPosition]) } }
        }

        private fun loadImage(imageUrl: String) {
            Glide.with(itemView.viewedItemPicIv.context)
                .load(imageUrl)
                .error(R.drawable.ic_catalog_item_stub)
                .into(itemView.viewedItemPicIv)
        }
    }

}