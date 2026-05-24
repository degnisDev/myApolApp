package com.example.myaple_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String = "client" // Nuevo campo para control de acceso
) : java.io.Serializable
