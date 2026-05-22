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
        
        val sampleProducts = listOf(
            Product("iPhone 15 Pro", "$ 5.500.000", R.drawable.iphone_15_pro, "High-end smartphone"),
            Product("Apple Watch S9", "$ 1.800.000", R.drawable.apple_watch, "Smartwatch"),
            Product("MacBook Air M2", "$ 4.500.000", R.drawable.mack_book_air_m2, "Laptop"),
            Product("AirPods Pro 2", "$ 1.200.000", R.drawable.airpods, "Wireless earbuds")
        )

        rvStock.layoutManager = LinearLayoutManager(this)
        rvStock.adapter = AdminStockAdapter(sampleProducts)
    }
}