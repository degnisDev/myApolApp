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
import com.example.myaple_app.data.model.Product

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
        holder.tvPrice.text = "$ ${product.price}"
        
        // Carga de imagen (Revertido a la lógica original)
        val imageResId = if (!product.imageUrl.isNullOrEmpty()) {
            context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
        } else 0

        if (imageResId != 0) {
            holder.ivProduct.setImageResource(imageResId)
        } else {
            holder.ivProduct.setImageResource(R.drawable.logo_app)
        }

        // Acción 1: Clic en la tarjeta para ver detalles
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_DATA", product)
            context.startActivity(intent)
        }

        // Acción 2: Clic en "Ver más" / "Agregar"
        holder.btnAdd.setOnClickListener {
            Toast.makeText(context, "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productList.size
}
