package com.example.myaple_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myaple_app.R
import com.example.myaple_app.supabaseClient.client
import com.example.myaple_app.ui.auth.RegisterActivity
import com.example.myaple_app.ui.catalog.CatalogActivity
import com.example.myaple_app.ui.admin.AdminDashboardActivity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
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

        findViewById<LinearLayout>(R.id.btnAdminLogin)?.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }

        // Se comenta Seller por ahora ya que la actividad no existe y detiene la compilación
        /*
        findViewById<LinearLayout>(R.id.btnSellerLogin)?.setOnClickListener {
            // startActivity(Intent(this, SellerDashboardActivity::class.java))
        }
        */
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Ingresa tus credenciales")
            return
        }

        lifecycleScope.launch {
            try {
                btnLogin.isEnabled = false
                btnLogin.text = "Iniciando sesión..."

                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                showToast("¡Bienvenido!")
                val intent = Intent(this@MainActivity, CatalogActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                showToast("Error: Usuario o contraseña incorrectos")
                btnLogin.isEnabled = true
                btnLogin.text = "LOG IN"
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
