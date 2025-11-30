package com.example.appmovilev2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val tiempoDeEspera = 2000L // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            verificarSesion()
        }, tiempoDeEspera)
    }

    private fun verificarSesion() {
        val usuarioActual = FirebaseAuth.getInstance().currentUser

        if (usuarioActual != null) {
            // El usuario ya existe, vamos directo al Home
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            // No hay usuario, vamos al Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish() // Matamos el Splash para que no se pueda volver
    }
}