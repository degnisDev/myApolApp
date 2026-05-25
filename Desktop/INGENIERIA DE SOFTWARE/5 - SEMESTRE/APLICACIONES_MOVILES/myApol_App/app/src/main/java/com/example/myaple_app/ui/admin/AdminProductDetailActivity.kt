package com.example.myaple_app.ui.admin

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product
import com.example.myaple_app.supabaseClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminProductDetailActivity : AppCompatActivity() {

    private var currentProduct: Product? = null

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etStock: EditText
    private lateinit var etCategory: EditText
    private lateinit var etDescription: EditText
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_product_detail)

        setupViews()
        handleIntentData()
        setupListeners()
    }

    private fun setupViews() {
        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etStock = findViewById(R.id.etProductStock)
        etCategory = findViewById(R.id.etProductCategory)
        etDescription = findViewById(R.id.etProductDescription)
        tvTitle = findViewById(R.id.tvTitle)

        // Hacer que categoría no sea editable por teclado para usar el diálogo
        etCategory.isFocusable = false
        etCategory.isClickable = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topBar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun handleIntentData() {
        currentProduct = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PRODUCT_DATA", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("PRODUCT_DATA") as? Product
        }

        currentProduct?.let { product ->
            tvTitle.text = "Edit Product"
            etName.setText(product.name)
            etPrice.setText(product.price.toString())
            etStock.setText(product.stock.toString())
            etCategory.setText(product.category)
            etDescription.setText(product.description)
        } ?: run {
            tvTitle.text = "Add New Product"
        }
    }

    private fun setupListeners() {
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Selector de Categoría (Evita errores de escritura)
        etCategory.setOnClickListener { showCategoryDialog() }

        findViewById<AppCompatButton>(R.id.btnSaveProduct).setOnClickListener {
            saveProduct()
        }
    }

    private fun showCategoryDialog() {
        val categories = arrayOf("Smartphones", "Laptops", "Wearables", "Audio")
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Category")
            .setItems(categories) { _, which ->
                etCategory.setText(categories[which])
            }
            .show()
    }

    private fun saveProduct() {
        val name = etName.text.toString().trim()
        val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
        val stock = etStock.text.toString().toIntOrNull() ?: 0
        val category = etCategory.text.toString()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty() || price <= 0) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Creamos el objeto para guardar. 
        // Importante: preservamos el imageUrl original si estamos editando.
        val productToSave = Product(
            id = currentProduct?.id,
            name = name,
            description = description,
            price = price,
            stock = stock,
            category = category,
            imageUrl = currentProduct?.imageUrl 
        )

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    supabaseClient.client.postgrest["products"].upsert(productToSave)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminProductDetailActivity, "Changes saved successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminProductDetailActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
