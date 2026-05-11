package com.example.myaple_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navegación al registro
        findViewById<TextView>(R.id.tvSignUp)?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Navegación al catálogo
        findViewById<Button>(R.id.btnLogin)?.setOnClickListener {
            startActivity(Intent(this, CatalogActivity::class.java))
        }

        // Navegación al Dashboard de Administrador
        findViewById<LinearLayout>(R.id.btnAdminLogin)?.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }

        // Navegación al Dashboard de Vendedor
        findViewById<LinearLayout>(R.id.btnSellerLogin)?.setOnClickListener {
            startActivity(Intent(this, SellerDashboardActivity::class.java))
        }
    }
}