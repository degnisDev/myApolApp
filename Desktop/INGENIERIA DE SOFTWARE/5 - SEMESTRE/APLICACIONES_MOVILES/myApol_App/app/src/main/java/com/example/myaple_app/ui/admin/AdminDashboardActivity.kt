package com.example.myaple_app.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myaple_app.R
import com.example.myaple_app.ui.main.MainActivity

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)
        
        // Ajuste de márgenes para el diseño de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Abrir la gestión de productos del inventario
        findViewById<LinearLayout>(R.id.btnStockManagement).setOnClickListener {
            val intent = Intent(this, AdminStockActivity::class.java)
            startActivity(intent)
        }

        // Abrir la gestión de perfiles de usuario
        findViewById<LinearLayout>(R.id.btnUserManagement).setOnClickListener {
            val intent = Intent(this, AdminUserActivity::class.java)
            startActivity(intent)
        }

        // Cerrar sesión y regresar a la pantalla de login
        findViewById<LinearLayout>(R.id.btnLogoutAction).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}