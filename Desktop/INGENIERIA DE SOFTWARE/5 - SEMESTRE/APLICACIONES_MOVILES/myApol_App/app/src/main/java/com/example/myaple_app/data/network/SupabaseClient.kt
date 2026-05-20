package com.example.myaple_app.data.network

import io.github.jan.tennert.supabase.createSupabaseClient
import io.github.jan.tennert.supabase.postgrest.Postgrest
import io.github.jan.tennert.supabase.auth.Auth

/**
 * Configuración global de Supabase.
 * Pega aquí tu URL y tu API KEY (anon) de la consola de Supabase.
 */
object SupabaseClient {
    private const val SUPABASE_URL = "https://TU_URL_AQUI.supabase.co"
    private const val SUPABASE_KEY = "TU_API_KEY_AQUI"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }
}
