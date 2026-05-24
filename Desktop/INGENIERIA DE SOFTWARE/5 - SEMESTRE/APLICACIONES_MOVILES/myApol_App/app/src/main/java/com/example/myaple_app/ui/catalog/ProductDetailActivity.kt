package com.example.myaple_app.ui.catalog

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

        // Ajuste de barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar el producto enviado desde el adaptador
        val product = intent.getSerializableExtra("PRODUCT_DATA") as? Product

        if (product != null) {
            displayProductDetails(product)
        }

        // Botón Volver
        findViewById<TextView>(R.id.tvBackDetail).setOnClickListener {
            finish()
        }

        // Botón Añadir al Carrito
        findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            Toast.makeText(this, "${product?.name} añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayProductDetails(product: Product) {
        val imgProduct: ImageView = findViewById(R.id.imgProductDetail)
        val tvName: TextView = findViewById(R.id.tvProductNameDetail)
        val tvPrice: TextView = findViewById(R.id.tvProductPriceDetail)
        val tvDesc: TextView = findViewById(R.id.tvProductDescDetail)

        tvName.text = product.name
        tvPrice.text = "$ ${product.price}"
        tvDesc.text = product.description

        // Carga de imagen (usando la lógica de recursos locales que tenemos)
        val imageResId = if (!product.imageUrl.isNullOrEmpty()) {
            resources.getIdentifier(product.imageUrl, "drawable", packageName)
        } else 0

        if (imageResId != 0) {
            imgProduct.setImageResource(imageResId)
        } else {
            imgProduct.setImageResource(R.drawable.logo_app)
        }
    }
}
