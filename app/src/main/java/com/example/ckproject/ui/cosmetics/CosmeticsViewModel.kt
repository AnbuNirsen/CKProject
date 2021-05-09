package com.example.ckproject.ui.cosmetics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ckproject.data.repository.CosmeticsRepository
import com.example.ckproject.utils.BRAND_NAME
import com.example.ckproject.utils.Resource
import kotlinx.coroutines.Dispatchers

class CosmeticsViewModel(private val cosmeticsRepository: CosmeticsRepository) : ViewModel() {

    fun getCosmetics() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = cosmeticsRepository.getCosmetics(BRAND_NAME)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: " Error occured!"))
        }
    }
}