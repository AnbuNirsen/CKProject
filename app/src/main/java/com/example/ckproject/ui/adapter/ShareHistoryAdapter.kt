package com.example.ckproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ckproject.R
import com.example.ckproject.data.database.ShareHistory
import java.text.SimpleDateFormat
import java.util.*

class ShareHistoryAdapter(val context: Context, val shareHistoryList: ArrayList<ShareHistory>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_share_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
    }

    override fun getItemCount() = shareHistoryList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_product_image: ImageView = itemView.findViewById(R.id.iv_product_image)
        val tv_product_name: TextView = itemView.findViewById(R.id.tv_product_name)
        val tv_time: TextView = itemView.findViewById(R.id.tv_time)
        val tv_share: TextView = itemView.findViewById(R.id.tv_share)

        fun bind(position: Int) {
            tv_product_name.text = shareHistoryList[position].productName
            val sdf = SimpleDateFormat("dd/MM")
            val resultdate = Date(shareHistoryList[position].time.toLong())
            tv_time.text = sdf.format(resultdate)

            tv_share.text = shareHistoryList[position].shareVia
            Glide.with(context)
                .load(shareHistoryList[position].imageUrl)
                .timeout(2000)
                .centerCrop()
                .placeholder(R.drawable.ic_cosmetics)
                .into(iv_product_image)
        }

    }

    fun addShareHistory(shareHistory: List<ShareHistory>) {
        this.shareHistoryList.apply {
            clear()
            addAll(shareHistory)
        }
    }
}