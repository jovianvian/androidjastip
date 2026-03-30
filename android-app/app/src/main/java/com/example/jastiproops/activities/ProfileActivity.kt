package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityProfileBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.User
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var b: ActivityProfileBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        b.btnUpdate.setOnClickListener { updateProfile() }
        b.btnLogout.setOnClickListener {
            session.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    override fun onResume() { super.onResume(); loadProfile() }

    private fun loadProfile() {
        ApiClient.service.getProfile(session.userId()).enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(call: Call<ApiResponse<User>>, response: Response<ApiResponse<User>>) {
                val u = response.body()?.data ?: return
                b.etName.setText(u.name)
                b.etPhone.setText(u.phone ?: "")
                b.tvEmail.text = u.email
                b.tvRole.text = u.role
            }
            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) { Ui.toast(this@ProfileActivity, "Gagal load profil") }
        })
    }

    private fun updateProfile() {
        val name = b.etName.text.toString().trim()
        val phone = b.etPhone.text.toString().trim()
        val pass = b.etPassword.text.toString().trim()

        if (name.isEmpty()) { Ui.toast(this, "Nama wajib"); return }

        ApiClient.service.updateProfile(session.userId(), name, phone, pass)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@ProfileActivity, response.body()?.message ?: "Update sukses"); b.etPassword.setText("") }
                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@ProfileActivity, "Gagal update") }
            })
    }
}
