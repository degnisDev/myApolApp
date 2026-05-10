package com.example.myaple_app

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        
        // Ajuste para el diseño a pantalla completa
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // --- LÓGICA DE NAVEGACIÓN ---

        // Configuración del texto "Back" (En reemplazo de la flecha)
        val tvBack = findViewById<TextView>(R.id.tvBack)
        tvBack?.setOnClickListener {
            finish() // Vuelve al Login
        }

        // Configuración del enlace inferior
        val tvToLogin = findViewById<TextView>(R.id.tvToLogin)
        tvToLogin?.setOnClickListener {
            finish() // Vuelve al Login
        }
    }
}