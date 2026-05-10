package com.example.myaple_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CatalogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalog)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        rvProducts.layoutManager = GridLayoutManager(this, 2)

        // Lista de productos con sus descripciones
        val productList = listOf(
            Product("iPhone 17 Pro Max", "$ 5.000.000", R.drawable.iphone17_pro_max, 
                "El iPhone 17 Pro Max lleva la fotografía móvil al siguiente nivel con su sistema de triple cámara de 48 MP."),
            Product("iPhone 16 Pro Max", "$ 3.500.000", R.drawable.iphone_16_pro_max, 
                "El iPhone 16 Pro incorpora el innovador botón de Control de Cámara para precisión profesional."),
            Product("iPhone 15 Pro", "$ 2.500.000", R.drawable.iphone_15_pro, 
                "Fabricado en titanio aeroespacial con chip A17 Pro y zoom óptico 5x."),
            Product("MacBook Air M2", "$ 4.500.000", R.drawable.mack_book_air_m2, 
                "Redefine lo que significa ser ligera y potente. 18 horas de batería."),
            Product("AirPods", "$ 2.500.000", R.drawable.airpods, 
                "Chip H2 mejorado y Cancelación Activa de Ruido de siguiente nivel."),
            Product("Apple Watch", "$ 1.000.000", R.drawable.apple_watch, 
                "Compañero definitivo para tu bienestar con monitorización avanzada.")
        )

        rvProducts.adapter = ProductAdapter(productList)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_cart -> {
                    // Futura pantalla de carrito
                    true
                }
                R.id.nav_profile -> {
                    // NAVEGACIÓN AL PERFIL
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
