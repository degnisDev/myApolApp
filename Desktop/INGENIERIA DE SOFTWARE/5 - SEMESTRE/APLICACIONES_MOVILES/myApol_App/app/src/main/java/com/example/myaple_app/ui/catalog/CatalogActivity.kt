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

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                insets
            }
        }

        // DATOS DE EJEMPLO ACTUALIZADOS CON IMÁGENES REALES
        val products = listOf(
            Product(id = 1, name = "iPhone 15 Pro", price = 5500000.0, description = "Apple smartphone", stock = 10, imageUrl = "iphone_15_pro"),
            Product(id = 2, name = "Apple Watch S9", price = 1800000.0, description = "Apple watch", stock = 15, imageUrl = "apple_watch"),
            Product(id = 3, name = "MacBook Air M2", price = 4500000.0, description = "Apple laptop", stock = 5, imageUrl = "mack_book_air_m2"),
            Product(id = 4, name = "AirPods Pro 2", price = 1200000.0, description = "Wireless earbuds", stock = 20, imageUrl = "airpods")
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
