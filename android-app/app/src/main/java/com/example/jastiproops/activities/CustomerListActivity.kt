package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jastiproops.adapters.CustomerAdapter
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityCustomerListBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.Customer
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerListActivity : AppCompatActivity() {
    private lateinit var b: ActivityCustomerListBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCustomerListBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        adapter = CustomerAdapter(mutableListOf(), ::editCustomer, ::deleteCustomer)
        b.rvCustomers.layoutManager = LinearLayoutManager(this)
        b.rvCustomers.adapter = adapter

        b.btnAddCustomer.setOnClickListener { startActivity(Intent(this, CustomerFormActivity::class.java)) }
        b.btnSearch.setOnClickListener { loadData() }
    }

    override fun onResume() { super.onResume(); loadData() }

    private fun loadData() {
        ApiClient.service.getCustomers(session.userId(), b.etSearch.text.toString().trim())
            .enqueue(object : Callback<ApiResponse<List<Customer>>> {
                override fun onResponse(call: Call<ApiResponse<List<Customer>>>, response: Response<ApiResponse<List<Customer>>>) {
                    adapter.replaceData(response.body()?.data ?: emptyList())
                }
                override fun onFailure(call: Call<ApiResponse<List<Customer>>>, t: Throwable) { Ui.toast(this@CustomerListActivity, "Gagal load customer") }
            })
    }

    private fun editCustomer(item: Customer) {
        val i = Intent(this, CustomerFormActivity::class.java)
        i.putExtra("id", item.id)
        i.putExtra("name", item.name)
        i.putExtra("phone", item.phone)
        i.putExtra("address", item.address)
        i.putExtra("notes", item.notes ?: "")
        startActivity(i)
    }

    private fun deleteCustomer(item: Customer) {
        AlertDialog.Builder(this)
            .setMessage("Hapus customer ${item.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                ApiClient.service.deleteCustomer(session.userId(), item.id).enqueue(object : Callback<ApiResponse<Any>> {
                    override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                        Ui.toast(this@CustomerListActivity, response.body()?.message ?: "Selesai")
                        loadData()
                    }
                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@CustomerListActivity, "Gagal hapus") }
                })
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
