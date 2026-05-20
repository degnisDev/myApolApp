package com.example.myaple_app.ui.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myaple_app.R

class AdminProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_product_detail)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topBar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Botón Atrás
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Botón Guardar
        findViewById<AppCompatButton>(R.id.btnSaveProduct).setOnClickListener {
            // Simulación de guardado para Front-end
            Toast.makeText(this, "Product saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
