package com.example.jastiproops.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val phone: String? = null,
    val is_active: Int? = 1
)
