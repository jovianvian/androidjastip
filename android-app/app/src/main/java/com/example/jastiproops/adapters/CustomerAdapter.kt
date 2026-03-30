package com.example.jastiproops.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jastiproops.R
import com.example.jastiproops.models.Customer

class CustomerAdapter(
    private val items: MutableList<Customer>,
    private val onEdit: (Customer) -> Unit,
    private val onDelete: (Customer) -> Unit
) : RecyclerView.Adapter<CustomerAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = v.findViewById(R.id.tvSubtitle)
        val btnEdit: ImageButton = v.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_action, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.tvTitle.text = c.name
        holder.tvSubtitle.text = "${c.phone} | ${c.address}"
        holder.btnEdit.setOnClickListener { onEdit(c) }
        holder.btnDelete.setOnClickListener { onDelete(c) }
    }

    fun replaceData(newItems: List<Customer>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
