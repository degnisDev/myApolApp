package com.example.myaple_app.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.CartItem
import java.util.Locale

class CartAdapter(
    private var cartList: List<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onDeleteItem: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.imgCartProduct)
        val tvName: TextView = view.findViewById(R.id.tvCartProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        val product = item.product

        holder.tvName.text = product?.name ?: "Unknown Product"
        holder.tvPrice.text = String.format(Locale.getDefault(), "$%,.0f", product?.price ?: 0.0)
        holder.tvQuantity.text = item.quantity.toString()

        // Carga de imagen (recursos locales)
        val context = holder.itemView.context
        val imageResId = if (!product?.imageUrl.isNullOrEmpty()) {
            context.resources.getIdentifier(product?.imageUrl, "drawable", context.packageName)
        } else 0
        
        if (imageResId != 0) {
            holder.imgProduct.setImageResource(imageResId)
        } else {
            holder.imgProduct.setImageResource(R.drawable.logo_app)
        }

        // Lógica de botones
        holder.btnPlus.setOnClickListener {
            onQuantityChanged(item, item.quantity + 1)
        }

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                onQuantityChanged(item, item.quantity - 1)
            }
        }

        holder.btnDelete.setOnClickListener {
            onDeleteItem(item)
        }
    }

    override fun getItemCount(): Int = cartList.size

    fun updateData(newList: List<CartItem>) {
        this.cartList = newList
        notifyDataSetChanged()
    }
}
