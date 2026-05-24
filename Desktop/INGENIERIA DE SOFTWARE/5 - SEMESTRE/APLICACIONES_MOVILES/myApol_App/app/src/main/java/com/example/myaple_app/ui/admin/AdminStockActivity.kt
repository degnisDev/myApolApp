package com.example.myaple_app.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminStockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_stock)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<FloatingActionButton>(R.id.fabAddProduct).setOnClickListener {
            val intent = Intent(this, AdminProductDetailActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvStock = findViewById<RecyclerView>(R.id.rvStock)
        
        // Ajustamos los datos de prueba al modelo REAL Product.kt
        val sampleProducts = listOf(
            Product(id = 1, name = "iPhone 15 Pro", price = 5500000.0, stock = 10, description = "High-end smartphone", imageUrl = null),
            Product(id = 2, name = "Apple Watch S9", price = 1800000.0, stock = 5, description = "Smartwatch", imageUrl = null),
            Product(id = 3, name = "MacBook Air M2", price = 4500000.0, stock = 3, description = "Laptop", imageUrl = null)
        )

        rvStock.layoutManager = LinearLayoutManager(this)
        rvStock.adapter = AdminStockAdapter(sampleProducts)
    }
}