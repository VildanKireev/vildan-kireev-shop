package com.kireev.shop.domain.model.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteCategory(
    val name: String,
    val products: List<RemoteProduct>
) : Parcelable