package com.kireev.shop.domain

import com.kireev.shop.domain.model.remote.RemoteCategory
import com.kireev.shop.domain.model.remote.RemoteOrder
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface MainApi {

    @GET("products/allWithCategories/Kireev/")
    suspend fun allProductsWithCategories(): List<RemoteCategory>

    @POST("orders/new/Kireev/")
    suspend fun createOrder(@Body order: RemoteOrder)
}