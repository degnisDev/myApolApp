package com.example.myaple_app.ui.admin

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaple_app.R
import com.example.myaple_app.data.model.User
import com.example.myaple_app.supabaseClient.client
import io.github.jan.supabase.postgrest.postgrest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminUserActivity : AppCompatActivity() {

    private lateinit var adapter: AdminUserAdapter
    private val userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_user)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            showToast("To add users, use SignUp screen")
        }

        setupRecyclerView()
        fetchUsers()
    }

    private fun setupRecyclerView() {
        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        adapter = AdminUserAdapter(
            userList,
            onEditClick = { user -> showRoleDialog(user) },
            onDeleteClick = { user -> showDeleteConfirmation(user) }
        )
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = adapter
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                val users = client.postgrest["profiles"].select().decodeList<User>()
                userList.clear()
                userList.addAll(users)
                adapter.updateData(userList)
                Log.d("ADMIN_CRUD", "Fetch: ${users.size} users found")
            } catch (e: Exception) {
                Log.e("ADMIN_CRUD", "Fetch failed", e)
                showToast("Error loading: ${e.message}")
            }
        }
    }

    private fun showRoleDialog(user: User) {
        val roles = arrayOf("admin", "seller", "client")
        AlertDialog.Builder(this)
            .setTitle("Change role for ${user.name}")
            .setItems(roles) { _, which ->
                updateUserRole(user, roles[which])
            }
            .show()
    }

    private fun updateUserRole(user: User, newRole: String) {
        lifecycleScope.launch {
            try {
                val updateData = mapOf("role" to newRole)
                client.postgrest["profiles"].update(updateData) {
                    filter { eq("id", user.id) }
                }
                showToast("Role updated to $newRole")
                delay(800)
                fetchUsers()
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showDeleteConfirmation(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Remove ${user.name} permanently?")
            .setPositiveButton("Delete") { _, _ -> deleteUser(user) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            try {
                Log.d("ADMIN_CRUD", "Deleting user ID: ${user.id}")
                
                // Punto 4.3: DELETE - Operación real
                client.postgrest["profiles"].delete {
                    filter {
                        eq("id", user.id)
                    }
                }
                
                showToast("User deleted from DB")
                
                // Limpieza visual inmediata antes de refrescar
                userList.remove(user)
                adapter.updateData(userList)
                
                delay(500)
                fetchUsers() // Refresco final de seguridad
                
            } catch (e: Exception) {
                Log.e("ADMIN_CRUD", "Delete failed", e)
                showToast("Error deleting: ${e.message}")
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
