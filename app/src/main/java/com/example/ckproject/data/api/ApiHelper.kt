package com.example.ckproject.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getCosmetics(brand: String) = apiService.getCosmetics(brand)
}