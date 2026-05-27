package com.example.myaple_app.ui.seller

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myaple_app.R
import com.example.myaple_app.data.model.User
import com.example.myaple_app.supabaseClient.client
import com.example.myaple_app.ui.main.MainActivity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class SellerDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seller_dashboard)
        
        // Ajuste de los márgenes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Traer datos reales del vendedor
        val user = client.auth.currentUserOrNull()
        lifecycleScope.launch {
            try {
                val profile = client.postgrest["profiles"].select {
                    filter { eq("id", user?.id ?: "") }
                }.decodeSingle<User>()
                
                findViewById<TextView>(R.id.tvSellerName).text = profile.name
                findViewById<TextView>(R.id.tvSellerEmail).text = profile.email
                findViewById<TextView>(R.id.tvSellerRole).text = profile.role
            } catch (e: Exception) {
                findViewById<TextView>(R.id.tvSellerEmail).text = user?.email
                findViewById<TextView>(R.id.tvSellerRole).text = getString(R.string.seller)
            }
        }

        // 2. Configuración de la acción de salida
        findViewById<LinearLayout>(R.id.btnLogoutAction).setOnClickListener {
            lifecycleScope.launch {
                client.auth.signOut()
                val intent = Intent(this@SellerDashboardActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}