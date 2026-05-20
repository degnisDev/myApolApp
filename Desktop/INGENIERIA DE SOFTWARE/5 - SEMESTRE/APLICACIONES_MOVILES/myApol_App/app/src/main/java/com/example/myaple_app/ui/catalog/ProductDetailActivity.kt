package com.example.myaple_app.ui.catalog

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        @Suppress("DEPRECATION")
        val product = intent.getSerializableExtra("PRODUCT_DATA") as? Product

        if (product != null) {
            val imgDetail = findViewById<ImageView>(R.id.imgProductDetail)
            val tvName = findViewById<TextView>(R.id.tvProductNameDetail)
            val tvPrice = findViewById<TextView>(R.id.tvProductPriceDetail)
            val tvDesc = findViewById<TextView>(R.id.tvProductDescDetail)
            val tvBack = findViewById<TextView>(R.id.tvBackDetail)

            // Llenamos la información (Ajustado al nuevo modelo de datos)
            tvName.text = product.name
            tvPrice.text = "$ ${product.price}"
            tvDesc.text = product.description
            // imgDetail.setImageResource(product.imageRes) // Ajustar según cómo manejes imágenes en Supabase

            tvBack.setOnClickListener { finish() }
        }
    }
}
