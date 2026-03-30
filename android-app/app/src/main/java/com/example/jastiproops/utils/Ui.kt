package com.example.jastiproops.utils

import android.content.Context
import android.widget.Toast

object Ui {
    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
