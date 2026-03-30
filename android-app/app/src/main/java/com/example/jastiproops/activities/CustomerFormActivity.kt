package com.example.jastiproops.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityCustomerFormBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.IdResponse
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerFormActivity : AppCompatActivity() {
    private lateinit var b: ActivityCustomerFormBinding
    private lateinit var session: SessionManager
    private var customerId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCustomerFormBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        customerId = intent.getIntExtra("id", 0)

        if (customerId > 0) {
            b.etName.setText(intent.getStringExtra("name") ?: "")
            b.etPhone.setText(intent.getStringExtra("phone") ?: "")
            b.etAddress.setText(intent.getStringExtra("address") ?: "")
            b.etNotes.setText(intent.getStringExtra("notes") ?: "")
        }

        b.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = b.etName.text.toString().trim()
        val phone = b.etPhone.text.toString().trim()
        val address = b.etAddress.text.toString().trim()
        val notes = b.etNotes.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Ui.toast(this, "Nama, HP, alamat wajib")
            return
        }

        if (customerId > 0) {
            ApiClient.service.updateCustomer(session.userId(), customerId, name, phone, address, notes)
                .enqueue(object : Callback<ApiResponse<Any>> {
                    override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@CustomerFormActivity, response.body()?.message ?: "Sukses"); finish() }
                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@CustomerFormActivity, "Gagal update") }
                })
        } else {
            ApiClient.service.createCustomer(session.userId(), name, phone, address, notes)
                .enqueue(object : Callback<ApiResponse<IdResponse>> {
                    override fun onResponse(call: Call<ApiResponse<IdResponse>>, response: Response<ApiResponse<IdResponse>>) { Ui.toast(this@CustomerFormActivity, response.body()?.message ?: "Sukses"); finish() }
                    override fun onFailure(call: Call<ApiResponse<IdResponse>>, t: Throwable) { Ui.toast(this@CustomerFormActivity, "Gagal simpan") }
                })
        }
    }
}
