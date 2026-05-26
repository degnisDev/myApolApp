package com.example.myaple_app.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.example.myaple_app.data.model.CartItem
import com.example.myaple_app.data.model.User
import com.example.myaple_app.supabaseClient.client
import com.example.myaple_app.ui.catalog.CatalogActivity
import com.example.myaple_app.ui.main.MainActivity
import com.example.myaple_app.ui.main.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private lateinit var tvTotalValue: TextView
    private var cartItems: List<CartItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        checkUserRoleAndInitialize()
    }

    private fun checkUserRoleAndInitialize() {
        val user = client.auth.currentUserOrNull()
        if (user == null) {
            redirectToLogin()
            return
        }

        lifecycleScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    client.postgrest["profiles"].select {
                        filter { eq("id", user.id) }
                    }.decodeSingle<User>()
                }

                if (profile.role == "client") {
                    setupUI()
                    setupRecyclerView()
                    setupBottomNavigation()
                    fetchCartItems()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CartActivity, "Access denied: Clients only", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                redirectToLogin()
            }
        }
    }

    private fun setupUI() {
        tvTotalValue = findViewById(R.id.tvTotalValue)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        findViewById<Button>(R.id.btnCheckout).setOnClickListener {
            if (cartItems.isNotEmpty()) {
                startActivity(Intent(this, PaymentActivity::class.java))
            } else {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val rvCart = findViewById<RecyclerView>(R.id.rvCartItems)
        rvCart.layoutManager = LinearLayoutManager(this)
        
        adapter = CartAdapter(
            cartList = emptyList(),
            onQuantityChanged = { item, newQuantity ->
                updateQuantityInSupabase(item, newQuantity)
            },
            onDeleteItem = { item ->
                deleteItemFromSupabase(item)
            }
        )
        rvCart.adapter = adapter
    }

    private fun fetchCartItems() {
        val user = client.auth.currentUserOrNull() ?: return
        
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    // LLAMADA CLAVE: Realizamos un JOIN con la tabla products
                    // Usamos el alias 'product' para que coincida con el nombre en CartItem.kt
                    client.postgrest["cart_items"].select(Columns.raw("*, product:products(*)")) {
                        filter { eq("user_id", user.id) }
                    }.decodeList<CartItem>()
                }
                
                cartItems = response
                withContext(Dispatchers.Main) {
                    adapter.updateData(cartItems)
                    calculateTotal()
                }
                
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    adapter.updateData(emptyList())
                    tvTotalValue.text = "$0"
                }
            }
        }
    }

    private fun calculateTotal() {
        var total = 0.0
        cartItems.forEach { item ->
            // Ahora item.product ya no será null, traerá los datos de Supabase
            val price = item.product?.price ?: 0.0
            total += price * item.quantity
        }
        tvTotalValue.text = String.format(Locale.getDefault(), "$%,.0f", total)
    }

    private fun updateQuantityInSupabase(item: CartItem, newQuantity: Int) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    client.postgrest["cart_items"].update({
                        set("quantity", newQuantity)
                    }) {
                        filter { eq("id", item.id ?: -1) }
                    }
                }
                fetchCartItems()
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteItemFromSupabase(item: CartItem) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    client.postgrest["cart_items"].delete {
                        filter { eq("id", item.id ?: -1) }
                    }
                }
                fetchCartItems()
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_cart
        bottomNav.setOnItemSelectedListener { navItem ->
            when (navItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, CatalogActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_cart -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
