package com.example.ckproject.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ckproject.data.api.ApiHelper
import com.example.ckproject.data.database.ShareHistoryDatabase
import com.example.ckproject.data.repository.CosmeticsRepository
import com.example.ckproject.ui.cosmetics.CosmeticsViewModel

class CosmeticsViewModelFactory(private val apiHelper: ApiHelper,private val shareHistoryDatabase: ShareHistoryDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CosmeticsViewModel::class.java)) {
            return CosmeticsViewModel(CosmeticsRepository(apiHelper,shareHistoryDatabase)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}