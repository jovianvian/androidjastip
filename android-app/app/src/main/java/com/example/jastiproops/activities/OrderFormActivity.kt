package com.example.jastiproops.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityOrderFormBinding
import com.example.jastiproops.models.*
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderFormActivity : AppCompatActivity() {
    private lateinit var b: ActivityOrderFormBinding
    private lateinit var session: SessionManager
    private var customers: List<Customer> = emptyList()
    private var products: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOrderFormBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        loadCustomers()
        loadProducts()

        b.btnCalc.setOnClickListener { updateTotalPreview() }
        b.btnSave.setOnClickListener { submit() }
    }

    private fun loadCustomers() {
        ApiClient.service.getCustomers(session.userId()).enqueue(object : Callback<ApiResponse<List<Customer>>> {
            override fun onResponse(call: Call<ApiResponse<List<Customer>>>, response: Response<ApiResponse<List<Customer>>>) {
                customers = response.body()?.data ?: emptyList()
                b.spCustomer.adapter = ArrayAdapter(this@OrderFormActivity, android.R.layout.simple_spinner_dropdown_item, customers.map { "${it.name} - ${it.phone}" })
            }
            override fun onFailure(call: Call<ApiResponse<List<Customer>>>, t: Throwable) { Ui.toast(this@OrderFormActivity, "Gagal load customer") }
        })
    }

    private fun loadProducts() {
        ApiClient.service.getProducts(session.userId()).enqueue(object : Callback<ApiResponse<List<Product>>> {
            override fun onResponse(call: Call<ApiResponse<List<Product>>>, response: Response<ApiResponse<List<Product>>>) {
                products = response.body()?.data ?: emptyList()
                b.spProduct.adapter = ArrayAdapter(this@OrderFormActivity, android.R.layout.simple_spinner_dropdown_item, products.map { "${it.name} (Rp ${it.price})" })
            }
            override fun onFailure(call: Call<ApiResponse<List<Product>>>, t: Throwable) { Ui.toast(this@OrderFormActivity, "Gagal load produk") }
        })
    }

    private fun updateTotalPreview() {
        val qty = b.etQty.text.toString().trim().toIntOrNull() ?: 0
        val p = products.getOrNull(b.spProduct.selectedItemPosition)
        val total = qty * (p?.price ?: 0)
        b.tvTotal.text = "Total: Rp $total"
    }

    private fun submit() {
        if (customers.isEmpty() || products.isEmpty()) { Ui.toast(this, "Data customer/produk belum siap"); return }
        val qty = b.etQty.text.toString().trim().toIntOrNull() ?: 0
        if (qty <= 0) { Ui.toast(this, "Jumlah harus > 0"); return }
        val customer = customers[b.spCustomer.selectedItemPosition]
        val product = products[b.spProduct.selectedItemPosition]
        val notes = b.etNotes.text.toString().trim()

        ApiClient.service.createOrder(session.userId(), customer.id, product.id, qty, notes)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@OrderFormActivity, response.body()?.message ?: "Sukses"); finish() }
                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@OrderFormActivity, "Gagal simpan order") }
            })
    }
}
