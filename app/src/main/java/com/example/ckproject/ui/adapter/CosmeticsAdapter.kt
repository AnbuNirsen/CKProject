package com.example.ckproject.ui.adapter

import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ckproject.R
import com.example.ckproject.data.model.CosmeticsItem
import com.example.ckproject.ui.broadcaste_receiver.ShareIntentResultReceiver
import com.example.ckproject.utils.CREAM_CATEGORY


class CosmeticsAdapter(val context: Context, val cosmeticsList: ArrayList<CosmeticsItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val receiver1 = ShareIntentResultReceiver()

    var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MySharedPref", MODE_PRIVATE)

    var edit = sharedPreferences.edit()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            ViewHolder1(
                LayoutInflater.from(context).inflate(R.layout.row_item_one, parent, false)
            )
        } else {
            ViewHolder2(
                LayoutInflater.from(context).inflate(R.layout.row_item_two, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (cosmeticsList.get(position).category.equals("") || cosmeticsList.get(position).category.equals(
                CREAM_CATEGORY
            )
        )
            (holder as ViewHolder2).bind(position)
        else
            (holder as ViewHolder1).bind(position)

    }

    override fun getItemCount() = cosmeticsList.size

    inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.tv_name)
        var description: TextView = itemView.findViewById(R.id.tv_description)
        var price: TextView = itemView.findViewById(R.id.tv_price)
        var iv_item: ImageView = itemView.findViewById(R.id.iv_item)
        var btn_share: Button = itemView.findViewById(R.id.btn_share)
        fun bind(position: Int) {
            itemName.text = cosmeticsList.get(position).name
            description.text = cosmeticsList.get(position).description
            price.text =
                cosmeticsList.get(position).price_sign + " " + cosmeticsList.get(position).price
            Glide.with(context)
                .load(cosmeticsList.get(position).image_link)
                .timeout(2000)
                .centerCrop()
                .placeholder(R.drawable.ic_cosmetics)
                .into(iv_item)
            btn_share.setOnClickListener {
                clearPref()
                saveIntoSharedPref(cosmeticsList[position].name, cosmeticsList[position].image_link)
                shareIntent(position)
            }
        }

    }

    private fun saveIntoSharedPref(name: String, url: String) {
        edit.putString("time", System.currentTimeMillis().toString())
        edit.putString("productName", name)
        edit.putString("image_url", url)
        edit.commit()
    }

    private fun clearPref() {
        edit.clear()
    }

    inner class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.tv_name)
        var description: TextView = itemView.findViewById(R.id.tv_description)
        var price: TextView = itemView.findViewById(R.id.tv_price)
        var iv_item: ImageView = itemView.findViewById(R.id.iv_item)
        var btn_share: Button = itemView.findViewById(R.id.btn_share)

        fun bind(position: Int) {
            itemName.text = cosmeticsList.get(position).name
            description.text = cosmeticsList.get(position).description
            price.text =
                cosmeticsList.get(position).price_sign + " " + cosmeticsList.get(position).price
            Glide.with(context)
                .load(cosmeticsList.get(position).image_link)
                .timeout(2000)
                .centerCrop()
                .placeholder(R.drawable.ic_cosmetics)
                .into(iv_item)
            btn_share.setOnClickListener {
                clearPref()
                saveIntoSharedPref(cosmeticsList[position].name, cosmeticsList[position].image_link)
                shareIntent(position)
            }

        }

    }

    fun shareIntent(position: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_SUBJECT, cosmeticsList[position].name)
        intent.putExtra(
            Intent.EXTRA_TEXT,
            cosmeticsList[position].description + "\n" + cosmeticsList[position].image_link
        )
        intent.type = "text/plain"

        val shareAction = "com.example.ckproject.SHARE_ACTION"
        val receiver = Intent(shareAction)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT)
        val chooser = Intent.createChooser(intent, "test", pendingIntent.intentSender)
        context.registerReceiver(receiver1, IntentFilter(shareAction))

        context.startActivity(chooser)
    }

    override fun getItemViewType(position: Int): Int {
        if (cosmeticsList.get(position).category.equals("") || cosmeticsList.get(position).category.equals(
                CREAM_CATEGORY
            )
        )
            return 2
        else
            return 1
    }

    fun addCosmetics(cosmetics: ArrayList<CosmeticsItem>) {
        this.cosmeticsList.apply {
            clear()
            addAll(cosmetics)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        context.unregisterReceiver(receiver1)
    }
}