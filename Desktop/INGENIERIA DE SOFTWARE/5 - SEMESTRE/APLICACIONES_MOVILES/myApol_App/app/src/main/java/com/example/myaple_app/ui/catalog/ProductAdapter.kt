package com.example.myaple_app.ui.catalog

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.CartItem
import com.example.myaple_app.data.model.Product
import com.example.myaple_app.supabaseClient.client
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        val context = holder.itemView.context
        
        holder.tvName.text = product.name
        holder.tvPrice.text = String.format(Locale.getDefault(), "$%,.0f", product.price)
        
        val imageResId = if (!product.imageUrl.isNullOrEmpty()) {
            context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
        } else 0

        if (imageResId != 0) {
            holder.ivProduct.setImageResource(imageResId)
        } else {
            holder.ivProduct.setImageResource(R.drawable.logo_app)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_DATA", product)
            context.startActivity(intent)
        }

        holder.btnAdd.setOnClickListener {
            addToCart(product, holder)
        }
    }

    private fun addToCart(product: Product, holder: ProductViewHolder) {
        val user = client.auth.currentUserOrNull()
        if (user == null) {
            Toast.makeText(holder.itemView.context, "Please log in to add items", Toast.LENGTH_SHORT).show()
            return
        }

        // Usamos GlobalScope o una referencia al scope de la Activity si fuera posible, 
        // pero para el adaptador usaremos CoroutineScope manual para este evento.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                holder.btnAdd.isEnabled = false
                holder.btnAdd.text = "Adding..."

                withContext(Dispatchers.IO) {
                    // Lógica: Verificar si ya existe para sumar cantidad, o insertar nuevo
                    val existingItem = client.postgrest["cart_items"].select {
                        filter {
                            eq("user_id", user.id)
                            eq("product_id", product.id ?: -1)
                        }
                    }.decodeSingleOrNull<CartItem>()

                    if (existingItem != null) {
                        // Update
                        client.postgrest["cart_items"].update({
                            set("quantity", existingItem.quantity + 1)
                        }) {
                            filter { eq("id", existingItem.id ?: -1) }
                        }
                    } else {
                        // Insert
                        val newItem = CartItem(
                            user_id = user.id,
                            product_id = product.id ?: -1,
                            quantity = 1
                        )
                        client.postgrest["cart_items"].insert(newItem)
                    }
                }

                Toast.makeText(holder.itemView.context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(holder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                holder.btnAdd.isEnabled = true
                holder.btnAdd.text = "Add to cart"
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}
