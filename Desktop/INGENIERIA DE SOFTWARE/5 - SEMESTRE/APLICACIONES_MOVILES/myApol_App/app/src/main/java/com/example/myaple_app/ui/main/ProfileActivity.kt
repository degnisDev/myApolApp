package com.example.myaple_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myaple_app.R
import com.example.myaple_app.supabaseClient.client
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Configuración de insets para el diseño a pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        // Listener para la acción de logout en el panel de perfil
        findViewById<LinearLayout>(R.id.btnLogoutAction).setOnClickListener {
            performLogout()
        }
    }

    // Lógica para finalizar la sesión del usuario en Supabase y redirigir al login
    private fun performLogout() {
        lifecycleScope.launch {
            try {
                client.auth.signOut()
                Toast.makeText(this@ProfileActivity, "Sesión finalizada", Toast.LENGTH_SHORT).show()
                
                // Redirección a la pantalla principal y limpieza del stack de actividades
                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error al cerrar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
