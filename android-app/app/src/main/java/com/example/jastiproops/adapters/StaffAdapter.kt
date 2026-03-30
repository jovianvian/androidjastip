package com.example.jastiproops.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jastiproops.R
import com.example.jastiproops.models.User

class StaffAdapter(private val items: MutableList<User>) : RecyclerView.Adapter<StaffAdapter.VH>() {
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = v.findViewById(R.id.tvSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_row_simple, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val s = items[position]
        holder.tvTitle.text = s.name
        holder.tvSubtitle.text = "${s.email} | ${if (s.is_active == 1) "Aktif" else "Nonaktif"}"
    }

    fun replaceData(newItems: List<User>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
