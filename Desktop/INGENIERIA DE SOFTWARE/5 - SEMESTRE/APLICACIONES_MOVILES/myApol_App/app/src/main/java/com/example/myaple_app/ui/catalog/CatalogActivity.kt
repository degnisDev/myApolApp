package com.example.myaple_app.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product
import com.example.myaple_app.supabaseClient.client
import com.example.myaple_app.ui.cart.CartActivity
import com.example.myaple_app.ui.main.MainActivity
import com.example.myaple_app.ui.main.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class CatalogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalog)

        // Configuración de insets para el diseño a pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Configuración del botón de cierre de sesión
        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            performLogout()
        }

        // Definición de la lista de productos para mostrar en el catálogo
        val products = listOf(
            Product(7, "iPhone 17 Pro Max 256 GB", "El iPhone 17 Pro Max cuenta con el chip A19 Pro y un avanzado sistema de cámaras.", 5000000.0, 10, null, "iphone17_pro_max"),
            Product(2, "iPhone 16 Pro 512 GB", "Introduce un nuevo control de cámara y un rendimiento optimizado.", 3500000.0, 15, null, "iphone_16_pro_max"),
            Product(3, "iPhone 15 Pro Max 256 GB", "Fabricado en titanio y con un zoom óptico potente para fotografía de alta calidad.", 2500000.0, 20, null, "iphone_15_pro"),
            Product(4, "MacBook Air Chip M2", "La combinación ideal de portabilidad y potencia con pantalla Liquid Retina.", 4500000.0, 8, null, "mack_book_air_m2"),
            Product(5, "Apple Watch Series 9", "Reloj inteligente con funciones avanzadas de salud y nuevos gestos táctiles.", 1000000.0, 25, null, "apple_watch"),
            Product(6, "AirPods Pro 2da Gen", "Ofrecen el doble de cancelación activa de ruido y audio espacial personalizado.", 700000.0, 20, null, "airpods")
        )

        // Inicialización del listado de productos en formato de cuadrícula
        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        rvProducts.layoutManager = GridLayoutManager(this, 2)
        rvProducts.adapter = ProductAdapter(products)

        // Gestión de la navegación inferior
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
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // Proceso asíncrono para cerrar la sesión actual en Supabase
    private fun performLogout() {
        lifecycleScope.launch {
            try {
                client.auth.signOut()
                Toast.makeText(this@CatalogActivity, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
                
                // Redirección al login y limpieza del historial de navegación
                val intent = Intent(this@CatalogActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CatalogActivity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
