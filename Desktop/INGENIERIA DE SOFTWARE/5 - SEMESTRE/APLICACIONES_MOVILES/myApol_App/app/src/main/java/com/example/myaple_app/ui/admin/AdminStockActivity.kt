package com.example.myaple_app.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product
import com.example.myaple_app.supabaseClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AdminStockActivity : AppCompatActivity() {

    private lateinit var adapter: AdminStockAdapter
    private var selectedProduct: Product? = null
    
    private lateinit var btnEditTop: ImageView
    private lateinit var btnDeleteTop: ImageView
    private lateinit var tvTotalUnits: TextView
    private lateinit var tvTotalValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_stock)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupRecyclerView()
        fetchProducts()
    }

    private fun setupUI() {
        btnEditTop = findViewById(R.id.btnEditTop)
        btnDeleteTop = findViewById(R.id.btnDeleteTop)
        tvTotalUnits = findViewById(R.id.tvTotalUnits)
        tvTotalValue = findViewById(R.id.tvTotalValue)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Botón para abrir el formulario de creación de producto
        findViewById<FloatingActionButton>(R.id.fabAddProduct).setOnClickListener {
            val intent = Intent(this, AdminProductDetailActivity::class.java)
            startActivity(intent)
        }

        // Configuración de edición para el producto seleccionado
        btnEditTop.setOnClickListener {
            selectedProduct?.let { product ->
                val intent = Intent(this, AdminProductDetailActivity::class.java)
                intent.putExtra("PRODUCT_DATA", product)
                startActivity(intent)
            }
        }

        // Configuración de eliminación para el producto seleccionado
        btnDeleteTop.setOnClickListener {
            selectedProduct?.let { product ->
                showDeleteConfirmation(product)
            }
        }
    }

    private fun showDeleteConfirmation(product: Product) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar Producto")
            .setMessage("¿Estás seguro de que deseas eliminar ${product.name}? Esta acción no se puede deshacer.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                deleteProductFromSupabase(product)
            }
            .show()
    }

    private fun deleteProductFromSupabase(product: Product) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    supabaseClient.client.postgrest["products"].delete {
                        filter {
                            eq("id", product.id ?: -1)
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminStockActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    fetchProducts() // Actualizamos la lista tras borrar
                    selectedProduct = null
                    toggleActionButtons(false)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminStockActivity, "Error al eliminar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val rvStock = findViewById<RecyclerView>(R.id.rvStock)
        rvStock.layoutManager = LinearLayoutManager(this)
        
        adapter = AdminStockAdapter(emptyList()) { product ->
            selectedProduct = product
            toggleActionButtons(product != null)
        }
        rvStock.adapter = adapter
    }

    // Control visual de los botones de acción en la barra superior
    private fun toggleActionButtons(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        btnEditTop.visibility = visibility
        btnDeleteTop.visibility = visibility
    }

    // Consulta de productos a Supabase ordenados por ID
    private fun fetchProducts() {
        lifecycleScope.launch {
            try {
                val products = withContext(Dispatchers.IO) {
                    supabaseClient.client.postgrest["products"]
                        .select() {
                            order("id", Order.ASCENDING)
                        }
                        .decodeList<Product>()
                }
                
                withContext(Dispatchers.Main) {
                    adapter.updateData(products)
                    updateSummary(products)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminStockActivity, "Error al cargar stock: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Cálculo de los totales del inventario (unidades y valor monetario)
    private fun updateSummary(products: List<Product>) {
        val totalUnits = products.sumOf { it.stock }
        val totalValue = products.sumOf { it.price * it.stock }

        tvTotalUnits.text = totalUnits.toString()
        tvTotalValue.text = String.format(Locale.getDefault(), "$%,.0f", totalValue)
    }

    override fun onResume() {
        super.onResume()
        fetchProducts() // Refrescamos datos al volver de otras pantallas
        selectedProduct = null
        toggleActionButtons(false)
    }
}
