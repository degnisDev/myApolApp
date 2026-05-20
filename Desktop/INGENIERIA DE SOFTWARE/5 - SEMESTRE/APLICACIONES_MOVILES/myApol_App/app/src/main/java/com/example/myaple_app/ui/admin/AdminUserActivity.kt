package com.example.myaple_app.ui.admin

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
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

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            // Lógica para agregar usuario
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        
        // Datos de ejemplo para el Front-end
        // Nota: Asegúrate de que UserItem esté accesible o muévelo también a data.model
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

// Clase de datos temporal si no existe
data class UserItem(val name: String, val role: String)
