package com.kireev.shop.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kireev.shop.R
import kotlinx.android.synthetic.main.item_category_layout.view.*

class CategoriesAdapter(
    private val onCategoryClick: (pos: Int) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var categoriesList = listOf<String>()

    fun setList(categories: List<String>) {
        categoriesList = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_layout, parent, false)
    )

    override fun getItemCount(): Int = categoriesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoriesList[position], position == categoriesList.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(categoryName: String, isLast: Boolean) {
            itemView.apply {
                categoryNameTv.text = categoryName
                categoryCl.setOnClickListener {
                    onCategoryClick(adapterPosition)
                }
                categoryDivider.visibility = if (isLast) View.INVISIBLE else View.VISIBLE
            }

        }
    }
}