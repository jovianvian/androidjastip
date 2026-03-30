package com.example.jastiproops.activities

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityStaffFormBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffFormActivity : AppCompatActivity() {
    private lateinit var b: ActivityStaffFormBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityStaffFormBinding.inflate(layoutInflater)
        setContentView(b.root)
        session = SessionManager(this)
        b.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = b.etName.text.toString().trim()
        val email = b.etEmail.text.toString().trim()
        val phone = b.etPhone.text.toString().trim()
        val pass = b.etPassword.text.toString().trim()
        val active = if (b.switchActive.isChecked) 1 else 0

        if (name.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || pass.length < 6) {
            Ui.toast(this, "Data staff tidak valid")
            return
        }

        ApiClient.service.createStaff(session.userId(), name, email, phone, pass, active)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@StaffFormActivity, response.body()?.message ?: "Sukses"); finish() }
                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@StaffFormActivity, "Gagal tambah staff") }
            })
    }
}
