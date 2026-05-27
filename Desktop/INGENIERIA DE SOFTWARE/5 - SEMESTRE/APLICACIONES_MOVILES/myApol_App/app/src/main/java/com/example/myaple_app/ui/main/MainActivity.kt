package com.example.myaple_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myaple_app.R
import com.example.myaple_app.data.model.User
import com.example.myaple_app.supabaseClient.client
import com.example.myaple_app.ui.auth.RegisterActivity
import com.example.myaple_app.ui.catalog.CatalogActivity
import com.example.myaple_app.ui.admin.AdminDashboardActivity
import com.example.myaple_app.ui.seller.SellerDashboardActivity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()

        val mainView = findViewById<android.view.View>(R.id.main)
        mainView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
    }

    private fun setupListeners() {
        findViewById<TextView>(R.id.tvSignUp)?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showToast(getString(R.string.enter_credentials))
            return
        }

        lifecycleScope.launch {
            try {
                btnLogin.isEnabled = false
                btnLogin.text = getString(R.string.logging_in)

                // Autenticación con el servicio de Supabase
                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Recuperamos el ID del usuario autenticado
                val userId = client.auth.currentUserOrNull()?.id 
                    ?: throw Exception(getString(R.string.error_format, "Session not found"))

                // Consultamos el perfil para determinar el rol y dirigir a la vista correspondiente
                val userProfile = client.postgrest["profiles"].select {
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingle<User>()

                // Redirección basada en el rol del perfil
                when (userProfile.role) {
                    "admin" -> {
                        showToast(getString(R.string.access_admin))
                        val intent = Intent(this@MainActivity, AdminDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    "seller" -> {
                        showToast(getString(R.string.access_seller))
                        val intent = Intent(this@MainActivity, SellerDashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    else -> {
                        showToast(getString(R.string.welcome_msg))
                        val intent = Intent(this@MainActivity, CatalogActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
                finish()

            } catch (e: Exception) {
                showToast(getString(R.string.error_format, e.message))
                btnLogin.isEnabled = true
                btnLogin.text = getString(R.string.log_in).uppercase()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
