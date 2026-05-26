package com.example.myaple_app.ui.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.Product

class AdminStockAdapter(
    private var productList: List<Product>,
    private val onProductSelected: (Product?) -> Unit
) : RecyclerView.Adapter<AdminStockAdapter.StockViewHolder>() {

    // Variable para rastrear la posición del producto seleccionado en la lista
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.cardProduct)
        val tvId: TextView = view.findViewById(R.id.tvProductId)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvStock: TextView = view.findViewById(R.id.tvProductStock)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val tvCategory: TextView = view.findViewById(R.id.tvProductCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_product, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val product = productList[position]
        
        holder.tvId.text = product.id.toString()
        holder.tvName.text = product.name
        holder.tvStock.text = product.stock.toString()
        holder.tvPrice.text = "$${String.format("%,.0f", product.price)}"
        holder.tvCategory.text = product.category ?: "N/A"

        // Aplicamos un resaltado visual si el producto actual es el seleccionado
        if (selectedPosition == position) {
            holder.card.setCardBackgroundColor(Color.parseColor("#40FFFFFF")) 
        } else {
            holder.card.setCardBackgroundColor(Color.parseColor("#20FFFFFF"))
        }

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            if (selectedPosition == position) {
                // Si se toca el mismo producto, se deselecciona
                selectedPosition = RecyclerView.NO_POSITION
                onProductSelected(null)
            } else {
                // Marcamos la nueva posición como seleccionada
                selectedPosition = position
                onProductSelected(product)
            }
            // Notificamos cambios para refrescar el color de fondo de los items afectados
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }

    // Función para actualizar la lista de productos y resetear la selección
    fun updateData(newList: List<Product>) {
        this.productList = newList
        this.selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = productList.size
}
