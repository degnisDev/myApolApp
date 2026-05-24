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

    private fun performRegistration() {
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirm = etPasswordConfirm.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            showToast("Please fill all fields")
            return
        }

        if (password != confirm) {
            showToast("Passwords do not match")
            return
        }

        if (password.length < 6) {
            showToast("Password must be at least 6 characters")
            return
        }

        lifecycleScope.launch {
            try {
                btnRegister.isEnabled = false
                btnRegister.text = "Registering..."

                // 1. Auth SignUp
                client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                val userId = client.auth.currentUserOrNull()?.id 
                    ?: throw Exception("User ID not found")

                // 2. Create profile with default role "client" (Step 3 of the plan)
                val newUserProfile = User(
                    id = userId,
                    name = name,
                    email = email,
                    phone = phone,
                    role = "client"
                )

                // 3. Save to database
                client.postgrest["profiles"].insert(newUserProfile)

                showToast("Registration successful!")
                finish()

            } catch (e: Exception) {
                showToast("Error: ${e.message}")
                btnRegister.isEnabled = true
                btnRegister.text = "SIGN UP"
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
