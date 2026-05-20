package com.example.myaple_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myaple_app.R
import com.example.myaple_app.ui.auth.RegisterActivity
import com.example.myaple_app.ui.catalog.CatalogActivity
import com.example.myaple_app.ui.admin.AdminDashboardActivity
import com.example.myaple_app.ui.seller.SellerDashboardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.tvSignUp)?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogin)?.setOnClickListener {
            startActivity(Intent(this, CatalogActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnAdminLogin)?.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnSellerLogin)?.setOnClickListener {
            startActivity(Intent(this, SellerDashboardActivity::class.java))
        }
    }
}
