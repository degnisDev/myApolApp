package com.example.myaple_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)

        // Configuración para que el contenido no quede debajo de las barras del sistema
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Recibimos el objeto producto enviado desde el catálogo
        @Suppress("DEPRECATION")
        val product = intent.getSerializableExtra("PRODUCT_DATA") as? Product

        if (product != null) {
            // Referencias del Layout
            val imgDetail = findViewById<ImageView>(R.id.imgProductDetail)
            val tvName = findViewById<TextView>(R.id.tvProductNameDetail)
            val tvPrice = findViewById<TextView>(R.id.tvProductPriceDetail)
            val tvDesc = findViewById<TextView>(R.id.tvProductDescDetail)
            val tvBack = findViewById<TextView>(R.id.tvBackDetail)

            // Llenamos la información
            imgDetail.setImageResource(product.imageRes)
            tvName.text = product.name
            tvPrice.text = product.price
            tvDesc.text = product.description

            // Lógica para volver atrás
            tvBack.setOnClickListener { finish() }
        }
    }
}
