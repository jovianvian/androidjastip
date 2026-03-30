package com.example.jastiproops.session

import android.content.Context

class SessionManager(context: Context) {
    private val pref = context.getSharedPreferences("jastipro_session", Context.MODE_PRIVATE)

    fun saveLogin(id: Int, name: String, email: String, role: String) {
        pref.edit()
            .putBoolean("is_login", true)
            .putInt("user_id", id)
            .putString("name", name)
            .putString("email", email)
            .putString("role", role)
            .apply()
    }

    fun isLogin(): Boolean = pref.getBoolean("is_login", false)
    fun userId(): Int = pref.getInt("user_id", 0)
    fun name(): String = pref.getString("name", "") ?: ""
    fun email(): String = pref.getString("email", "") ?: ""
    fun role(): String = pref.getString("role", "") ?: ""
    fun isManager(): Boolean = role() == "manager"

    fun logout() {
        pref.edit().clear().apply()
    }
}
