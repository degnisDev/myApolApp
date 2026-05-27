package com.example.myaple_app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.User

class AdminUserAdapter(
    private var userList: List<User>,
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<AdminUserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvUserName)
        val tvRole: TextView = view.findViewById(R.id.tvUserRole)
        val btnEdit: ImageView = view.findViewById(R.id.btnEditUser)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        
        // Asignación de datos básicos del perfil a la vista
        holder.tvName.text = user.name
        holder.tvRole.text = holder.itemView.context.getString(R.string.role_format, user.role)

        // Listeners para editar el rol o eliminar al usuario
        holder.btnEdit.setOnClickListener { onEditClick(user) }
        holder.btnDelete.setOnClickListener { onDeleteClick(user) }
    }

    override fun getItemCount(): Int = userList.size

    // Actualización de la lista de usuarios tras operaciones CRUD
    fun updateData(newList: List<User>) {
        this.userList = newList
        notifyDataSetChanged()
    }
}
