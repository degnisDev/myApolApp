package com.example.myaple_app

import java.io.Serializable

//Creamos el constructor para los productos
data class Product(
    val name: String,
    val price: String,
    val imageRes: Int,
    val description: String
) : Serializable
