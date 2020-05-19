package com.kireev.shop.domain.model.local

import kotlinx.serialization.Serializable


@Serializable
data class CartItem(val product: Product, var count: Int = 1)