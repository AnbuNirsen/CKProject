package com.example.ckproject.ui.shareHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ckproject.data.repository.CosmeticsRepository
import com.example.ckproject.utils.Resource
import kotlinx.coroutines.Dispatchers

class ShareHistoryViewModel(private val cosmeticsRepository: CosmeticsRepository) : ViewModel() {

    fun getShareHistory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = cosmeticsRepository.getShareHistory()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: " Error occured!"))
        }
    }
}