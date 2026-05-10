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

        val productList = listOf(
            Product("iPhone 17 Pro Max", "$ 5.000.000", R.drawable.iphone17_pro_max, 
                "El iPhone 17 Pro Max lleva la fotografía móvil al siguiente nivel."),
            Product("iPhone 16 Pro Max", "$ 3.500.000", R.drawable.iphone_16_pro_max, 
                "El iPhone 16 Pro incorpora el innovador botón de Control de Cámara."),
            Product("iPhone 15 Pro", "$ 2.500.000", R.drawable.iphone_15_pro, 
                "Fabricado en titanio aeroespacial con chip A17 Pro."),
            Product("MacBook Air M2", "$ 4.500.000", R.drawable.mack_book_air_m2, 
                "La MacBook Air con chip M2 redefine lo que significa ser ligera."),
            Product("AirPods", "$ 2.500.000", R.drawable.airpods, 
                "Los AirPods Pro con chip H2 mejorado."),
            Product("Apple Watch", "$ 1.000.000", R.drawable.apple_watch, 
                "El Apple Watch Series 9 presenta el gesto Double Tap.")
        )

        rvProducts.adapter = ProductAdapter(productList)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_cart -> {
                    // Ahora apunta a la clase correcta: CartActivity
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
