package com.kireev.shop.domain.model.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteProduct(
    val id: String,
    val name: String,
    val price: Double,
    val discountPercent: Int,
    val description: String,
    val imageUrl: String,
    val attributes: List<Attribute>
) : Parcelable {
    @Parcelize
    data class Attribute(
        val name: String,
        val value: String
    ) : Parcelable
}

