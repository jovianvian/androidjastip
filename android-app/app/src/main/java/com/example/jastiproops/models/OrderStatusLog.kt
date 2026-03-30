package com.example.jastiproops.models

data class OrderStatusLog(
    val old_status: String?,
    val new_status: String,
    val notes: String,
    val changed_at: String
)
