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
        holder.tvPrice.text = String.format(Locale.getDefault(), context.getString(R.string.price_format_currency), product.price)
        
        // Carga de la imagen del producto desde los recursos locales del proyecto
        val imageResId = if (!product.imageUrl.isNullOrEmpty()) {
            context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
        } else 0

        if (imageResId != 0) {
            holder.ivProduct.setImageResource(imageResId)
        } else {
            holder.ivProduct.setImageResource(R.drawable.logo_app)
        }

        // Evento para visualizar el detalle completo del producto
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_DATA", product)
            context.startActivity(intent)
        }

        // Evento para añadir el producto al carrito de compras
        holder.btnAdd.setOnClickListener {
            addToCart(product, holder)
        }
    }

    // Lógica para gestionar la inserción o actualización de productos en el carrito (Supabase)
    private fun addToCart(product: Product, holder: ProductViewHolder) {
        val context = holder.itemView.context
        val user = client.auth.currentUserOrNull()
        if (user == null) {
            Toast.makeText(context, context.getString(R.string.login_required_add_cart), Toast.LENGTH_SHORT).show()
            return
        }

        // Ejecución en un hilo secundario para no bloquear la interfaz de usuario
        CoroutineScope(Dispatchers.Main).launch {
            try {
                holder.btnAdd.isEnabled = false
                holder.btnAdd.text = context.getString(R.string.adding)

                withContext(Dispatchers.IO) {
                    // Verificamos si el producto ya existe en el carrito del usuario
                    val existingItem = client.postgrest["cart_items"].select {
                        filter {
                            eq("user_id", user.id)
                            eq("product_id", product.id ?: -1)
                        }
                    }.decodeSingleOrNull<CartItem>()

                    if (existingItem != null) {
                        // Si ya existe, incrementamos la cantidad actual
                        client.postgrest["cart_items"].update({
                            set("quantity", existingItem.quantity + 1)
                        }) {
                            filter { eq("id", existingItem.id ?: -1) }
                        }
                    } else {
                        // Si es nuevo, insertamos el registro con cantidad inicial de 1
                        val newItem = CartItem(
                            user_id = user.id,
                            product_id = product.id ?: -1,
                            quantity = 1
                        )
                        client.postgrest["cart_items"].insert(newItem)
                    }
                }

                Toast.makeText(context, context.getString(R.string.product_added_msg, product.name), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_format, e.message), Toast.LENGTH_SHORT).show()
            } finally {
                holder.btnAdd.isEnabled = true
                holder.btnAdd.text = context.getString(R.string.add_to_cart)
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}
