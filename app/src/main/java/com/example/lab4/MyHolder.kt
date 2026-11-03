package com.example.lab4

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// tao ra cac doi tuong tu mo ta cua file xml
class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivIcon = itemView.findViewById<ImageView>(R.id.iv_icon)
    val tvName = itemView.findViewById<TextView>(R.id.tv_name)
    val tvDetail = itemView.findViewById<TextView>(R.id.tv_detail)
}
