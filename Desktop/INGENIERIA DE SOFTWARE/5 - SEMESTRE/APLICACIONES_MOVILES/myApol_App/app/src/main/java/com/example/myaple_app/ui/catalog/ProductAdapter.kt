package com.example.myaple_app.ui.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
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
        holder.tvPrice.text = "$ ${product.price}"
        
        // Buscamos dinámicamente la imagen en drawable usando el nombre guardado en imageUrl
        val imageResId = if (!product.imageUrl.isNullOrEmpty()) {
            context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
        } else 0

        if (imageResId != 0) {
            holder.ivProduct.setImageResource(imageResId)
        } else {
            // Si no se encuentra, ponemos el logo por defecto
            holder.ivProduct.setImageResource(R.drawable.logo_app)
        }
    }

    override fun getItemCount(): Int = productList.size
}
