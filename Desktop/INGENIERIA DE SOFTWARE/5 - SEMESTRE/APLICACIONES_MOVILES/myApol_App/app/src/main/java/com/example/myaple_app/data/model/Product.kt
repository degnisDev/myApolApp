package com.example.myaple_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long? = null, // BigInt en Supabase
    val name: String,
    val description: String? = null,
    val price: Double,
    val stock: Int,
    val category: String? = null,
    val imageUrl: String? = null
) : java.io.Serializable
