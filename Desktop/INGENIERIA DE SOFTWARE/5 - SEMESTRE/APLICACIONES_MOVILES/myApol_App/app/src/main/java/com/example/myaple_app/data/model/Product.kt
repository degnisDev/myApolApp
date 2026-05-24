package com.example.myaple_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int? = null,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imageUrl: String? = null
) : java.io.Serializable
