package com.example.jastiproops.models

data class OrderItem(
    val id: Int,
    val order_code: String,
    val customer_name: String,
    val product_name: String,
    val quantity: Int,
    val total_amount: Int,
    val status: String,
    val staff_name: String,
    val created_at: String
)
