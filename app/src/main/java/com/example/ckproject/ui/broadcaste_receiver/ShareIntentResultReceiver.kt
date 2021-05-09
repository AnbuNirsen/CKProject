package com.example.ckproject.ui.broadcaste_receiver

import android.content.*
import com.example.ckproject.data.database.ShareHistory
import com.example.ckproject.data.database.ShareHistoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareIntentResultReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val shareHistoryDatabase = ShareHistoryDatabase.getInstance(context.applicationContext)
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "MySharedPref",
            Context.MODE_PRIVATE
        )
        val edit = sharedPreferences.edit()
        val productName: String = sharedPreferences.getString("productName", "") ?: ""
        val time: String = sharedPreferences.getString("time", "") ?: ""
        val imageUrl: String = sharedPreferences.getString("image_url", "") ?: ""
        var shareVia: String = ""
        for (key in intent.extras!!.keySet()) {
            shareVia = intent.extras!![key].toString()
        }

        edit.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val shareHistory = ShareHistory(
                id = 0,
                productName = productName,
                time = time,
                imageUrl = imageUrl,
                shareVia = shareVia
            )
            shareHistoryDatabase.shareHistoryDao().insertData(shareHistory)
        }
        context.unregisterReceiver(this)
        val component = intent.getParcelableExtra<ComponentName>(Intent.EXTRA_CHOSEN_COMPONENT)
    }
}