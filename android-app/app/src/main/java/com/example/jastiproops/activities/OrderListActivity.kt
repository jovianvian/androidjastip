package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jastiproops.adapters.OrderAdapter
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityOrderListBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.OrderItem
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {
    private lateinit var b: ActivityOrderListBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        adapter = OrderAdapter(mutableListOf()) { order ->
            val i = Intent(this, OrderDetailActivity::class.java)
            i.putExtra("order_id", order.id)
            startActivity(i)
        }
        b.rvOrders.layoutManager = LinearLayoutManager(this)
        b.rvOrders.adapter = adapter

        b.btnAddOrder.setOnClickListener { startActivity(Intent(this, OrderFormActivity::class.java)) }
        b.btnFilter.setOnClickListener { loadData() }
    }

    override fun onResume() { super.onResume(); loadData() }

    private fun loadData() {
        ApiClient.service.getOrders(session.userId(), b.etStatus.text.toString().trim())
            .enqueue(object : Callback<ApiResponse<List<OrderItem>>> {
                override fun onResponse(call: Call<ApiResponse<List<OrderItem>>>, response: Response<ApiResponse<List<OrderItem>>>) { adapter.replaceData(response.body()?.data ?: emptyList()) }
                override fun onFailure(call: Call<ApiResponse<List<OrderItem>>>, t: Throwable) { Ui.toast(this@OrderListActivity, "Gagal load order") }
            })
    }
}
