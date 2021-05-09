package com.example.ckproject.data.model

data class CosmeticsItem(
    val description: String,
    val category: String,
    val id: Int,
    var image_link: String,
    val name: String,
    val price: String,
    val price_sign: String
)