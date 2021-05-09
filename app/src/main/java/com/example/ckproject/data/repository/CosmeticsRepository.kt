package com.example.ckproject.data.repository

import com.example.ckproject.data.api.ApiHelper
import com.example.ckproject.data.database.ShareHistory
import com.example.ckproject.data.database.ShareHistoryDatabase
import com.example.ckproject.data.model.Cosmetics
import com.example.ckproject.data.model.CosmeticsItem

class CosmeticsRepository(
    private val apiHelper: ApiHelper,
    val shareHistoryDatabase: ShareHistoryDatabase
) {
    suspend fun getCosmetics(brand: String): Cosmetics {
        val arrayList = ArrayList<CosmeticsItem>()
        apiHelper.getCosmetics(brand).forEach { cosmeticsItem ->
            val temp = cosmeticsItem.image_link.replace("http:", "https:")
            cosmeticsItem.apply {
                image_link = temp
            }
            arrayList.add(cosmeticsItem)
        }
        return Cosmetics().apply {
            clear()
            addAll(arrayList)
        }
    }

    suspend fun insertShareHistory(shareHistory: ShareHistory) {
        shareHistoryDatabase.shareHistoryDao().insertData(shareHistory)
    }

    suspend fun getShareHistory() = shareHistoryDatabase.shareHistoryDao().queryData()

}