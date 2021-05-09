package com.example.ckproject.data.api

import com.example.ckproject.data.model.Cosmetics
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/products.json")
    suspend fun getCosmetics(@Query("brand") brand: String): Cosmetics
}