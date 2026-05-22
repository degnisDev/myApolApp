package com.example.myaple_app.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myaple_app.R

class RegisterActivity : AppCompatActivity() {

    // 1. Declaramos las variables para las vistas
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

        // 2. Inicializamos las vistas (Mapeo con los IDs del XML)
        initViews()

        // 3. Configuramos las acciones (Clicks)
        setupListeners()

        // Manejo de diseño para pantallas modernas (Edge-to-edge)
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
        etFullName = findViewById(R.id.etFullName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmailReg)
        etPassword = findViewById(R.id.etPassReg)
        etPasswordConfirm = findViewById(R.id.etPassConf)
        btnRegister = findViewById(R.id.btnDoRegister)
    }

    private fun setupListeners() {
        // Botón Volver
        findViewById<TextView>(R.id.tvBack)?.setOnClickListener { finish() }

        // Texto "Ya tengo cuenta" (Redirige al login o cierra esta pantalla)
        findViewById<TextView>(R.id.tvToLogin)?.setOnClickListener { finish() }

        // Botón Registrarse (El gatillo para la funcionalidad de Supabase)
        btnRegister.setOnClickListener {
            val name = etFullName.text.toString()
            if (name.isNotEmpty()) {
                // Mensaje temporal para confirmar que el botón funciona
                Toast.makeText(this, "Intentando registrar a $name", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show()
            }
        }
    }
}