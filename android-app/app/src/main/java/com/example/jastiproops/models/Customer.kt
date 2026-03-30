package com.example.jastiproops.models

data class Customer(
    val id: Int,
    val name: String,
    val phone: String,
    val address: String,
    val notes: String? = ""
)
