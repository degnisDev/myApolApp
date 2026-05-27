package com.example.myaple_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myaple_app.R
import com.example.myaple_app.data.model.User
import com.example.myaple_app.supabaseClient.client
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 1. Traer datos reales
        val user = client.auth.currentUserOrNull()
        lifecycleScope.launch {
            try {
                val profile = client.postgrest["profiles"].select {
                    filter { eq("id", user?.id ?: "") }
                }.decodeSingle<User>()
                
                findViewById<TextView>(R.id.tvUserName).text = profile.name
                findViewById<TextView>(R.id.tvUserEmail).text = profile.email
                findViewById<TextView>(R.id.tvUserRole).text = profile.role
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvUserEmail).text = user?.email
                findViewById<TextView>(R.id.tvUserRole).text = getString(R.string.client)
            }
        }

        // 2. Botón Logout
        findViewById<LinearLayout>(R.id.btnLogoutAction).setOnClickListener {
            lifecycleScope.launch {
                client.auth.signOut()
                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
