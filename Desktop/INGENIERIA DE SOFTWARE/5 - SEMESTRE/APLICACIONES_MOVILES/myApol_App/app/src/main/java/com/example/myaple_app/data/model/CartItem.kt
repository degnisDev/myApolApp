package com.example.myaple_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Long? = null,           // ID del registro en cart_items
    val user_id: String? = null,    // UUID del usuario en Supabase
    val product_id: Long,           // ID del producto relacionado
    var quantity: Int = 1,          // Cantidad elegida
    val product: Product? = null    // Objeto producto completo (para mostrar nombre/precio)
) : java.io.Serializable
