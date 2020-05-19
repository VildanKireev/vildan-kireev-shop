package com.kireev.shop.domain.model.local

import com.kireev.shop.domain.model.remote.RemoteOrder

/**
 * модель для оформления заказа
 */
data class OrderModel(
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var paymentType: RemoteOrder.PaymentType = RemoteOrder.PaymentType.CashOnReceiving
)