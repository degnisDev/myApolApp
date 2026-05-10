package com.example.myaple_app

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        val btnPayNow = findViewById<Button>(R.id.btnPayNow)
        btnPayNow.setOnClickListener {
            // Mostramos el mensaje de éxito usando el recurso de strings
            Toast.makeText(this, getString(R.string.payment_success), Toast.LENGTH_LONG).show()
            
            // Opcional: Podríamos regresar al inicio después de unos segundos
            btnPayNow.postDelayed({
                finish()
            }, 2000)
        }
    }
}
