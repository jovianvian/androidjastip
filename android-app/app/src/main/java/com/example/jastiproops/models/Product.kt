package com.example.jastiproops.models

data class Product(
    val id: Int,
    val name: String,
    val description: String? = "",
    val price: Int,
    val origin_city: String,
    val eta_days: Int,
    val is_active: Int = 1
)
