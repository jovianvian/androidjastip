package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityLoginBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.LoginResponseData
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        if (session.isLogin()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        b.btnLogin.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        val email = b.etEmail.text.toString().trim()
        val password = b.etPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Ui.toast(this, "Format email tidak valid")
            return
        }
        if (password.isEmpty()) {
            Ui.toast(this, "Password wajib diisi")
            return
        }

        ApiClient.service.login(email, password).enqueue(object : Callback<ApiResponse<LoginResponseData>> {
            override fun onResponse(call: Call<ApiResponse<LoginResponseData>>, response: Response<ApiResponse<LoginResponseData>>) {
                val body = response.body()
                if (response.isSuccessful && body?.success == true && body.data != null) {
                    session.saveLogin(body.data.id, body.data.name, body.data.email, body.data.role)
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    Ui.toast(this@LoginActivity, body?.message ?: "Login gagal")
                }
            }

            override fun onFailure(call: Call<ApiResponse<LoginResponseData>>, t: Throwable) {
                Ui.toast(this@LoginActivity, "Gagal koneksi: ${t.message}")
            }
        })
    }
}
