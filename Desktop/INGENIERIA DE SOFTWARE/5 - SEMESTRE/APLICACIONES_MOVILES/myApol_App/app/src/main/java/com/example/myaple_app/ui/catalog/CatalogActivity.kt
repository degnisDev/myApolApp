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

        // Definición de la lista de productos para mostrar en el catálogo utilizando recursos de strings
        val products = listOf(
            Product(7, getString(R.string.product_iphone17_name), getString(R.string.product_iphone17_desc), 5000000.0, 10, null, "iphone17_pro_max"),
            Product(2, getString(R.string.product_iphone16_name), getString(R.string.product_iphone16_desc), 3500000.0, 15, null, "iphone_16_pro_max"),
            Product(3, getString(R.string.product_iphone15_name), getString(R.string.product_iphone15_desc), 2500000.0, 20, null, "iphone_15_pro"),
            Product(4, getString(R.string.product_macbook_name), getString(R.string.product_macbook_desc), 4500000.0, 8, null, "mack_book_air_m2"),
            Product(5, getString(R.string.product_watch_name), getString(R.string.product_watch_desc), 1000000.0, 25, null, "apple_watch"),
            Product(6, getString(R.string.product_airpods_name), getString(R.string.product_airpods_desc), 700000.0, 20, null, "airpods")
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
                Toast.makeText(this@CatalogActivity, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
                
                // Redirección al login y limpieza del historial de navegación
                val intent = Intent(this@CatalogActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CatalogActivity, getString(R.string.error_logout), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
