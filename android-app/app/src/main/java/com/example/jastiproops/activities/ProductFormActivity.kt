package com.example.jastiproops.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityProductFormBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.IdResponse
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFormActivity : AppCompatActivity() {
    private lateinit var b: ActivityProductFormBinding
    private lateinit var session: SessionManager
    private var productId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProductFormBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        productId = intent.getIntExtra("id", 0)

        if (productId > 0) {
            b.etName.setText(intent.getStringExtra("name") ?: "")
            b.etDescription.setText(intent.getStringExtra("description") ?: "")
            b.etPrice.setText(intent.getIntExtra("price", 0).toString())
            b.etOriginCity.setText(intent.getStringExtra("origin_city") ?: "")
            b.etEta.setText(intent.getIntExtra("eta_days", 0).toString())
            b.switchActive.isChecked = intent.getIntExtra("is_active", 1) == 1
        }

        b.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = b.etName.text.toString().trim()
        val desc = b.etDescription.text.toString().trim()
        val price = b.etPrice.text.toString().trim().toIntOrNull() ?: 0
        val origin = b.etOriginCity.text.toString().trim()
        val eta = b.etEta.text.toString().trim().toIntOrNull() ?: 0
        val isActive = if (b.switchActive.isChecked) 1 else 0

        if (name.isEmpty() || price <= 0 || origin.isEmpty() || eta <= 0) { Ui.toast(this, "Data produk tidak valid"); return }

        if (productId > 0) {
            ApiClient.service.updateProduct(session.userId(), productId, name, desc, price, origin, eta, isActive)
                .enqueue(object : Callback<ApiResponse<Any>> {
                    override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@ProductFormActivity, response.body()?.message ?: "Sukses"); finish() }
                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@ProductFormActivity, "Gagal update") }
                })
        } else {
            ApiClient.service.createProduct(session.userId(), name, desc, price, origin, eta, isActive)
                .enqueue(object : Callback<ApiResponse<IdResponse>> {
                    override fun onResponse(call: Call<ApiResponse<IdResponse>>, response: Response<ApiResponse<IdResponse>>) { Ui.toast(this@ProductFormActivity, response.body()?.message ?: "Sukses"); finish() }
                    override fun onFailure(call: Call<ApiResponse<IdResponse>>, t: Throwable) { Ui.toast(this@ProductFormActivity, "Gagal simpan") }
                })
        }
    }
}
