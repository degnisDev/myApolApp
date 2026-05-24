package com.example.myaple_app.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product
import com.example.myaple_app.ui.cart.CartActivity
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

        // --- RESTAURACIÓN DE CATÁLOGO A 6 PRODUCTOS (NOTAS-c) ---
        val products = listOf(
            Product(1, "iPhone 17 Pro Max 256 GB", "El iPhone 17 Pro Max lleva la fotografía móvil al siguiente nivel con su sistema de triple cámara de 48 MP. El potente chip A19 Pro garantiza un rendimiento inigualable en IA.", 5000000.0, 10, "iphone17_pro_max"),
            Product(2, "iPhone 16 Pro 512 GB", "El iPhone 16 Pro incorpora el innovador botón de Control de Cámara para capturar fotos y video con precisión profesional. Chip A18 Pro y pantalla Super Retina XDR.", 3500000.0, 15, "iphone_16_pro_max"),
            Product(3, "iPhone 15 Pro Max 256 GB", "Fabricado en titanio aeroespacial con chip A17 Pro, introduce el lente periscópico con zoom óptico 5x. Pantalla ProMotion de 6.7 pulgadas.", 2500000.0, 20, "iphone_15_pro"),
            Product(4, "MacBook Air Chip M2", "La MacBook Air con chip M2 redefine lo que significa ser ligera y potente. Con 18 horas de batería y pantalla Liquid Retina de 13.6 pulgadas.", 4500000.0, 8, "mack_book_air_m2"),
            Product(5, "Apple Watch Series 9", "Presenta el gesto Double Tap para controlar sin tocar la pantalla. Chip S9 SiP y monitorización de salud avanzada.", 1000000.0, 25, "apple_watch"),
            Product(6, "AirPods Pro 2da Gen", "Chip H2 y Cancelación Activa de Ruido 2x más potente. Audio Espacial dinámico personalizado y carga USB-C.", 700000.0, 20, "airpods")
        )

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        rvProducts.layoutManager = GridLayoutManager(this, 2)
        rvProducts.adapter = ProductAdapter(products)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Perfil en desarrollo", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
