package com.example.myaple_app.ui.auth

import android.os.Bundle
import android.view.View
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
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        initViews()
        setupListeners()

        // Ajuste de diseño para pantallas con notch o barras de sistema
        val mainView = findViewById<View>(R.id.main)
        mainView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    private fun initViews() {
        etFullName = findViewById(R.id.etFullName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmailReg)
        etPassword = findViewById(R.id.etPassReg)
        etPasswordConfirm = findViewById(R.id.etPassConf)
        btnRegister = findViewById(R.id.btnDoRegister)
    }

    private fun setupListeners() {
        findViewById<TextView>(R.id.tvBack)?.setOnClickListener { finish() }
        findViewById<TextView>(R.id.tvToLogin)?.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    // Lógica para procesar el registro de un nuevo usuario
    private fun performRegistration() {
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirm = etPasswordConfirm.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        // Validaciones básicas de formulario
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            showToast("Por favor completa todos los campos")
            return
        }

        if (password != confirm) {
            showToast("Las contraseñas no coinciden")
            return
        }

        if (password.length < 6) {
            showToast("La contraseña debe tener al menos 6 caracteres")
            return
        }

        lifecycleScope.launch {
            try {
                btnRegister.isEnabled = false
                btnRegister.text = "Registrando..."

                // Registro del usuario en el servicio de autenticación de Supabase
                client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                val userId = client.auth.currentUserOrNull()?.id 
                    ?: throw Exception("No se pudo obtener el ID del usuario")

                // Creación del objeto de perfil con el rol predeterminado 'client'
                val newUserProfile = User(
                    id = userId,
                    name = name,
                    email = email,
                    phone = phone,
                    role = "client"
                )

                // Persistencia del perfil de usuario en la tabla 'profiles'
                client.postgrest["profiles"].insert(newUserProfile)

                showToast("¡Registro exitoso!")
                finish()

            } catch (e: Exception) {
                showToast("Error en el registro: ${e.message}")
                btnRegister.isEnabled = true
                btnRegister.text = "SIGN UP"
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
