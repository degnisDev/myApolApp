package com.example.myaple_app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R

class AdminUserAdapter(private val userList: List<UserItem>) :
    RecyclerView.Adapter<AdminUserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUserIcon: ImageView = view.findViewById(R.id.ivUserIcon)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvUserRole: TextView = view.findViewById(R.id.tvUserRole)
        val btnEditUser: ImageView = view.findViewById(R.id.btnEditUser)
        val btnDeleteUser: ImageView = view.findViewById(R.id.btnDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        
        holder.tvUserName.text = user.name
        holder.tvUserRole.text = "Role: ${user.role}"
        holder.ivUserIcon.setImageResource(R.drawable.ic_profile)

        holder.btnEditUser.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit user: ${user.name}", Toast.LENGTH_SHORT).show()
        }

        holder.btnDeleteUser.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Remove user: ${user.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = userList.size
}
