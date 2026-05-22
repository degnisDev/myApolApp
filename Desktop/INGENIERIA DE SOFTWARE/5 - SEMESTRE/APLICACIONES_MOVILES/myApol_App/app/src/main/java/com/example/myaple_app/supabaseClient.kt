package com.example.myaple_app

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object supabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://udxyqajkkqprhrtrdwvk.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVkeHlxYWpra3FwcmhydHJkd3ZrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzkxOTM4NDgsImV4cCI6MjA5NDc2OTg0OH0.Fst7Vm9nxWyI2aRQ3Z1u0l05nCCQQIC2JwF8izDCayM"
    ){
        install(Postgrest)
        install(Auth)
    }
}