package com.example.lab4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val context: MainActivity) : RecyclerView.Adapter<MyHolder>() {

    // Tạo giao diện cho một dòng từ itemlayout.xml.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.itemlayout, parent, false)
        return MyHolder(view)
    }

    // Gán dữ liệu của phần tử tại position vào các View của dòng.
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = context.friendItems[position]
        holder.ivIcon.setImageResource(item.icon)
        holder.tvName.text = item.name
        holder.tvDetail.text = item.detail
    }

    override fun getItemCount(): Int = context.friendItems.size
}
