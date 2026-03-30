package com.example.jastiproops.models

data class OrderDetail(
    val id: Int,
    val order_code: String,
    val quantity: Int,
    val unit_price: Int,
    val total_amount: Int,
    val status: String,
    val notes: String?,
    val customer_name: String,
    val customer_phone: String,
    val customer_address: String,
    val product_name: String,
    val origin_city: String,
    val eta_days: Int,
    val staff_name: String,
    val status_logs: List<OrderStatusLog> = emptyList()
)
