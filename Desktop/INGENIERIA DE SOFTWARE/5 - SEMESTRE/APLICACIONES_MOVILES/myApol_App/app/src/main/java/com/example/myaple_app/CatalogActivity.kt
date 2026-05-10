package com.example.myaple_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CatalogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalog)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Dejamos el bottom en 0 para que la Bottom Navigation Bar se vea bien
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)

        // Configuramos el Grid de 2 columnas para una vista limpia
        rvProducts.layoutManager = GridLayoutManager(this, 2)

        // Cargamos la lista usando nuestra propia clase Product
        val productList = listOf(
            Product("iPhone 17 Pro Max", "$ 5.000.000", R.drawable.iphone17_pro_max),
            Product("iPhone 16 Pro Max", "$ 3.500.000", R.drawable.iphone_16_pro_max),
            Product("iPhone 15 Pro", "$ 2.500.000", R.drawable.iphone_15_pro),
            Product("MacBook Air M2", "$ 4.500.000", R.drawable.mack_book_air_m2),
            Product("AirPods", "$ 2.500.000", R.drawable.airpods),
            Product("Apple Watch", "$ 1.000.000", R.drawable.apple_watch)
        )

        rvProducts.adapter = ProductAdapter(productList)
    }
}
