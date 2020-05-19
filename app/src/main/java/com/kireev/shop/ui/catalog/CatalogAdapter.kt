package com.kireev.shop.ui.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kireev.shop.R
import com.kireev.shop.commonPriceFormat
import com.kireev.shop.domain.model.local.Product
import com.kireev.shop.strikethroughSpannable
import kotlinx.android.synthetic.main.item_catalog_layout.view.*
import kotlinx.android.synthetic.main.item_catalog_layout.view.catalogItemDescr
import kotlinx.android.synthetic.main.item_catalog_layout.view.catalogItemMainPrice
import kotlinx.android.synthetic.main.item_catalog_layout.view.catalogItemName
import kotlinx.android.synthetic.main.item_catalog_layout.view.catalogItemPicIv
import kotlinx.android.synthetic.main.item_catalog_layout.view.catalogItemRawPrice
import kotlinx.android.synthetic.main.item_catalog_layout.view.catalogItemRootCl

class CatalogAdapter(
    val onItemClick: (product: Product) -> Unit,
    val onAddToCartClick: (product: Product) -> Unit
) : RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    private var items = listOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_catalog_layout, parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setList(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            loadImage(product.imageUrl)
            setProductData(product)
            setListeners()
        }

        private fun loadImage(imageUrl: String) {
            Glide.with(itemView.catalogItemPicIv.context)
                .load(imageUrl)
                .error(R.drawable.ic_catalog_item_stub)
                .into(itemView.catalogItemPicIv)
        }

        private fun setProductData(product: Product) {
            itemView.apply {
                catalogItemName.text = product.productName
                catalogItemDescr.text = product.description
                if (product.salePercent != 0) {
                    catalogItemMainPrice.text = product.discountPrice.commonPriceFormat
                    catalogItemRawPrice.visibility = View.VISIBLE
                    catalogItemRawPrice.text =
                        product.price.commonPriceFormat.strikethroughSpannable
                } else {
                    catalogItemMainPrice.text = product.price.commonPriceFormat
                    catalogItemRawPrice.visibility = View.GONE
                }
            }
        }

        private fun setListeners() {
            itemView.apply {
                catalogItemRootCl.setOnClickListener {
                    onItemClick(items[adapterPosition])
                }
                catalogItemAddToCartIv.setOnClickListener {
                    onAddToCartClick(items[adapterPosition])
                }
            }
        }
    }
}
