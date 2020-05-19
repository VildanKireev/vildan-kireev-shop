package com.kireev.shop.domain.model.local

import android.os.Parcelable
import com.kireev.shop.domain.model.remote.RemoteProduct
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Product(
    val id: String,
    val price: Double,
    val salePercent: Int = 0,
    val productName: String,
    val imageUrl: String,
    val description: String,
    val attributes: List<String>
) : Parcelable {
    /**
     * цена с примененной скидкой, определяется по [salePercent]
     * Если [salePercent] больше 100, то цена товара будет отрицательной
     * Если [salePercent] меньше 0, то цена товара будет увеличена
     */
    val discountPrice: Double
        get() = price * (1 - salePercent / 100.0)
}


class ProductFactory {
    fun createProduct(remote: RemoteProduct): Product =
        Product(
            remote.id,
            remote.price,
            remote.discountPercent,
            remote.name,
            remote.imageUrl,
            remote.description,
            remote.attributes.map { it.name + ": " + it.value }
        )
}