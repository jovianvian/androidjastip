package com.example.jastiproops.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jastiproops.adapters.StaffAdapter
import com.example.jastiproops.api.ApiClient
import com.example.jastiproops.databinding.ActivityStaffListBinding
import com.example.jastiproops.models.ApiResponse
import com.example.jastiproops.models.User
import com.example.jastiproops.session.SessionManager
import com.example.jastiproops.utils.Ui
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffListActivity : AppCompatActivity() {
    private lateinit var b: ActivityStaffListBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: StaffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityStaffListBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionManager(this)
        if (!session.isManager()) { Ui.toast(this, "Hanya manager"); finish(); return }

        adapter = StaffAdapter(mutableListOf())
        b.rvStaff.layoutManager = LinearLayoutManager(this)
        b.rvStaff.adapter = adapter
        b.btnAddStaff.setOnClickListener { startActivity(Intent(this, StaffFormActivity::class.java)) }
    }

    override fun onResume() { super.onResume(); loadData() }

    private fun loadData() {
        ApiClient.service.getStaff(session.userId()).enqueue(object : Callback<ApiResponse<List<User>>> {
            override fun onResponse(call: Call<ApiResponse<List<User>>>, response: Response<ApiResponse<List<User>>>) { adapter.replaceData(response.body()?.data ?: emptyList()) }
            override fun onFailure(call: Call<ApiResponse<List<User>>>, t: Throwable) { Ui.toast(this@StaffListActivity, "Gagal load staff") }
        })
    }
}
