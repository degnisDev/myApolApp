package com.example.myaple_app

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_user)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón Atrás
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Botón Flotante para Agregar Usuario
        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            // Lógica para agregar usuario (Próxima Activity)
        }

        // Configuración de la lista de usuarios
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        
        // Datos de ejemplo para el Front-end
        val sampleUsers = listOf(
            UserItem("Geovany Quevedo", "Admin"),
            UserItem("Carlos Vendedor", "Seller"),
            UserItem("Ana Maria", "Seller"),
            UserItem("Admin Secundario", "Admin")
        )

        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = AdminUserAdapter(sampleUsers)
    }
}