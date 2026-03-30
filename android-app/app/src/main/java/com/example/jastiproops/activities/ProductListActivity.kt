package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jastiproops.adapters.ProductAdapter
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityProductListBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.Product
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity() {
    private lateinit var b: ActivityProductListBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        adapter = ProductAdapter(mutableListOf(), ::editProduct, ::deleteProduct)
        b.rvProducts.layoutManager = LinearLayoutManager(this)
        b.rvProducts.adapter = adapter

        b.btnAddProduct.setOnClickListener { startActivity(Intent(this, ProductFormActivity::class.java)) }
        b.btnSearch.setOnClickListener { loadData() }
    }

    override fun onResume() { super.onResume(); loadData() }

    private fun loadData() {
        ApiClient.service.getProducts(session.userId(), b.etSearch.text.toString().trim())
            .enqueue(object : Callback<ApiResponse<List<Product>>> {
                override fun onResponse(call: Call<ApiResponse<List<Product>>>, response: Response<ApiResponse<List<Product>>>) { adapter.replaceData(response.body()?.data ?: emptyList()) }
                override fun onFailure(call: Call<ApiResponse<List<Product>>>, t: Throwable) { Ui.toast(this@ProductListActivity, "Gagal load produk") }
            })
    }

    private fun editProduct(item: Product) {
        val i = Intent(this, ProductFormActivity::class.java)
        i.putExtra("id", item.id)
        i.putExtra("name", item.name)
        i.putExtra("description", item.description ?: "")
        i.putExtra("price", item.price)
        i.putExtra("origin_city", item.origin_city)
        i.putExtra("eta_days", item.eta_days)
        i.putExtra("is_active", item.is_active)
        startActivity(i)
    }

    private fun deleteProduct(item: Product) {
        AlertDialog.Builder(this)
            .setMessage("Hapus produk ${item.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                ApiClient.service.deleteProduct(session.userId(), item.id).enqueue(object : Callback<ApiResponse<Any>> {
                    override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@ProductListActivity, response.body()?.message ?: "Selesai"); loadData() }
                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@ProductListActivity, "Gagal hapus") }
                })
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
