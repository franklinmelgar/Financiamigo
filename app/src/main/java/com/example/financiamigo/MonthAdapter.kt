package com.example.financiamigo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MonthAdapter(private val months: List<String>, private var currentMonthPosition: Int) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.nombre_mes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mes_spinner, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.textView.text = months[position].toUpperCase()
    }

    override fun getItemCount(): Int {
        return months.size
    }
}