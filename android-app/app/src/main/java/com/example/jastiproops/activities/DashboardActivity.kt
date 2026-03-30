package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityDashboardBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.DashboardSummary
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var b: ActivityDashboardBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        if (!session.isLogin()) {
            goLogin()
            return
        }

        b.tvWelcome.text = "Halo, ${session.name()} (${session.role()})"
        b.btnCustomers.setOnClickListener { startActivity(Intent(this, CustomerListActivity::class.java)) }
        b.btnProducts.setOnClickListener { startActivity(Intent(this, ProductListActivity::class.java)) }
        b.btnOrders.setOnClickListener { startActivity(Intent(this, OrderListActivity::class.java)) }
        b.btnProfile.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        b.btnLogout.setOnClickListener {
            session.logout()
            goLogin()
        }
        if (session.isManager()) {
            b.btnStaff.visibility = View.VISIBLE
            b.btnStaff.setOnClickListener { startActivity(Intent(this, StaffListActivity::class.java)) }
        } else {
            b.btnStaff.visibility = View.GONE
        }

        loadSummary()
    }

    private fun loadSummary() {
        ApiClient.service.dashboard(session.userId()).enqueue(object : Callback<ApiResponse<DashboardSummary>> {
            override fun onResponse(call: Call<ApiResponse<DashboardSummary>>, response: Response<ApiResponse<DashboardSummary>>) {
                val d = response.body()?.data ?: return
                b.tvSummary.text = "Customer: ${d.total_customers}\nProduk: ${d.total_products}\nOrder: ${d.total_orders}\nPending: ${d.total_pending}\nSelesai: ${d.total_selesai}"
            }

            override fun onFailure(call: Call<ApiResponse<DashboardSummary>>, t: Throwable) {
                Ui.toast(this@DashboardActivity, "Gagal load dashboard")
            }
        })
    }

    private fun goLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
