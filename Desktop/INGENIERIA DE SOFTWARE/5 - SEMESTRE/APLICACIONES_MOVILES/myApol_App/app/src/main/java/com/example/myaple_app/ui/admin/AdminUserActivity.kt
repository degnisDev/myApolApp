package com.example.myaple_app.ui.admin

import android.content.Intent
import android.os.Bundle
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
import com.example.myaple_app.ui.auth.RegisterActivity
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
        
        // Configuración del botón para registrar nuevos usuarios desde el panel administrativo
        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
    }

    // Recargamos la lista al volver a la actividad para reflejar posibles cambios o nuevos registros
    override fun onResume() {
        super.onResume()
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

    // Obtención de todos los perfiles de usuario desde la tabla 'profiles' en Supabase
    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                val users = client.postgrest["profiles"].select().decodeList<User>()
                userList.clear()
                userList.addAll(users)
                adapter.updateData(userList)
            } catch (e: Exception) {
                showToast("Error al cargar usuarios: ${e.message}")
            }
        }
    }

    // Diálogo de selección para la actualización de roles de usuario
    private fun showRoleDialog(user: User) {
        val roles = arrayOf("admin", "seller", "client")
        AlertDialog.Builder(this)
            .setTitle("Cambiar rol para ${user.name}")
            .setItems(roles) { _, which ->
                updateUserRole(user, roles[which])
            }
            .show()
    }

    // Actualización del campo 'role' en la base de datos remota
    private fun updateUserRole(user: User, newRole: String) {
        lifecycleScope.launch {
            try {
                val updateData = mapOf("role" to newRole)
                client.postgrest["profiles"].update(updateData) {
                    filter { eq("id", user.id) }
                }
                showToast("Rol actualizado a $newRole")
                delay(800)
                fetchUsers()
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showDeleteConfirmation(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Estás seguro de que deseas eliminar a ${user.name} permanentemente?")
            .setPositiveButton("Eliminar") { _, _ -> deleteUser(user) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Eliminación lógica/física del perfil de usuario en la base de datos
    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            try {
                client.postgrest["profiles"].delete {
                    filter { eq("id", user.id) }
                }
                showToast("Usuario eliminado correctamente")
                userList.remove(user)
                adapter.updateData(userList)
                delay(500)
                fetchUsers()
            } catch (e: Exception) {
                showToast("Error al eliminar: ${e.message}")
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
