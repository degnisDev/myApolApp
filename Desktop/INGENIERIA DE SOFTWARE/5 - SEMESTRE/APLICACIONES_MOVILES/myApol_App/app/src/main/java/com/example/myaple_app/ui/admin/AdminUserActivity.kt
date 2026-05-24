package com.example.myaple_app.ui.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            Toast.makeText(this, "Para agregar, usa el Registro (SignUp)", Toast.LENGTH_SHORT).show()
        }

        setupRecyclerView()
        fetchUsers()
    }

    private fun setupRecyclerView() {
        val rvUsers = findViewById<RecyclerView>(R.id.rvUsers)
        adapter = AdminUserAdapter(userList) { user ->
            deleteUser(user)
        }
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = adapter
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            try {
                // Punto 4.3: READ del CRUD
                val users = client.postgrest["profiles"].select().decodeList<User>()
                userList.clear()
                userList.addAll(users)
                adapter.updateData(userList)
            } catch (e: Exception) {
                Toast.makeText(this@AdminUserActivity, "Error al cargar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            try {
                // Punto 4.3: DELETE del CRUD
                client.postgrest["profiles"].delete {
                    filter {
                        eq("id", user.id)
                    }
                }
                userList.remove(user)
                adapter.updateData(userList)
                Toast.makeText(this@AdminUserActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@AdminUserActivity, "Error al eliminar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
