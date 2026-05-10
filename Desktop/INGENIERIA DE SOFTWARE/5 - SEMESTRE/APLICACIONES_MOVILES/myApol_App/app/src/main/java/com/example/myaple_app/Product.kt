package com.example.myaple_app

import java.io.Serializable

data class Product(
    val name: String,
    val price: String,
    val imageRes: Int,
    val description: String
) : Serializable
