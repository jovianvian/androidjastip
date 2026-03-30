package com.example.jastiproops.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityOrderDetailBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.OrderDetail
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityOrderDetailBinding
    private lateinit var session: SessionManager
    private var orderId = 0
    private val statuses = listOf("pending", "diproses", "dibelikan", "dikirim", "selesai", "dibatalkan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        orderId = intent.getIntExtra("order_id", 0)
        b.spStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statuses)
        b.btnUpdateStatus.setOnClickListener { updateStatus() }
    }

    override fun onResume() { super.onResume(); loadDetail() }

    private fun loadDetail() {
        ApiClient.service.getOrderDetail(session.userId(), orderId).enqueue(object : Callback<ApiResponse<OrderDetail>> {
            override fun onResponse(call: Call<ApiResponse<OrderDetail>>, response: Response<ApiResponse<OrderDetail>>) {
                val d = response.body()?.data ?: return
                b.tvOrderCode.text = d.order_code
                b.tvDetail.text = "Customer: ${d.customer_name}\nProduk: ${d.product_name}\nQty: ${d.quantity}\nTotal: Rp ${d.total_amount}\nStatus: ${d.status}\nPetugas: ${d.staff_name}\nCatatan: ${d.notes ?: "-"}"
                val idx = statuses.indexOf(d.status)
                if (idx >= 0) b.spStatus.setSelection(idx)
            }
            override fun onFailure(call: Call<ApiResponse<OrderDetail>>, t: Throwable) { Ui.toast(this@OrderDetailActivity, "Gagal load detail") }
        })
    }

    private fun updateStatus() {
        val status = statuses[b.spStatus.selectedItemPosition]
        val note = b.etNote.text.toString().trim()
        ApiClient.service.updateOrderStatus(session.userId(), orderId, status, note)
            .enqueue(object : Callback<ApiResponse<Any>> {
                override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) { Ui.toast(this@OrderDetailActivity, response.body()?.message ?: "Sukses"); loadDetail() }
                override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) { Ui.toast(this@OrderDetailActivity, "Gagal update status") }
            })
    }
}
