package com.example.jastiproops.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jastiproops.R
import com.example.jastiproops.models.OrderItem

class OrderAdapter(
    private val items: MutableList<OrderItem>,
    private val onClick: (OrderItem) -> Unit
) : RecyclerView.Adapter<OrderAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = v.findViewById(R.id.tvSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_row_simple, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val o = items[position]
        holder.tvTitle.text = "${o.order_code} - ${o.customer_name}"
        holder.tvSubtitle.text = "${o.product_name} x${o.quantity} | ${o.status} | Rp ${o.total_amount}"
        holder.itemView.setOnClickListener { onClick(o) }
    }

    fun replaceData(newItems: List<OrderItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
