package com.example.myaple_app.ui.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R

class AdminStockAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<AdminStockAdapter.StockViewHolder>() {

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProductImage)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvStock: TextView = view.findViewById(R.id.tvProductStock)
        val btnEdit: ImageView = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_product, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val product = productList[position]
        
        holder.tvName.text = product.name
        holder.tvStock.text = "Stock: 10 units" // Placeholder para el front-end
        holder.ivProduct.setImageResource(product.imageRes)

        // Lleva al detalle del producto
        holder.btnEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_DATA", product)
            holder.itemView.context.startActivity(intent)
        }

        // Acción Eliminar: Simulación
        holder.btnDelete.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Removing: ${product.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productList.size
}