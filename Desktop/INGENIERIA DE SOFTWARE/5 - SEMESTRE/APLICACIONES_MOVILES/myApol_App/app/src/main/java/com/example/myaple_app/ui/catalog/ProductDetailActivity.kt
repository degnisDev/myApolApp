package com.example.myaple_app.ui.catalog

import android.os.Build
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

        // Ajuste de márgenes para que el contenido no quede oculto por las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperación segura del objeto Product (compatible con SDK 33+)
        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PRODUCT_DATA", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("PRODUCT_DATA") as? Product
        }

        if (product != null) {
            displayProductDetails(product)
        }

        // Configuración del botón para regresar a la pantalla anterior
        findViewById<TextView>(R.id.tvBackDetail).setOnClickListener {
            finish()
        }

        // Acción para agregar el producto al carrito (simulación por ahora)
        findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            Toast.makeText(this, "${product?.name} añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para vincular los datos del producto con los elementos de la interfaz
    private fun displayProductDetails(product: Product) {
        val imgProduct: ImageView = findViewById(R.id.imgProductDetail)
        val tvName: TextView = findViewById(R.id.tvProductNameDetail)
        val tvPrice: TextView = findViewById(R.id.tvProductPriceDetail)
        val tvDesc: TextView = findViewById(R.id.tvProductDescDetail)

        tvName.text = product.name
        tvPrice.text = "$ ${product.price}"
        tvDesc.text = product.description

        // Carga de la imagen utilizando el nombre del recurso guardado en la base de datos
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
